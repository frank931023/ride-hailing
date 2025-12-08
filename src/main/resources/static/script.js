const API_BASE = '/api/rides';
let currentRequest = null;
let allBids = [];
let drivers = {};
let driverAvailability = {};
let isInputting = false;  // 標記是否正在輸入
let refreshInterval = null;  // 刷新計時器

// 初始化
async function init() {
    await loadDrivers();
    await refreshCurrentRequest();
    // 使用可控制的計時器
    refreshInterval = setInterval(() => {
        if (!isInputting) {  // 只在不輸入時刷新
            refreshCurrentRequest();
        }
    }, 2000);
}

// 載入司機資訊
async function loadDrivers() {
    try {
        const response = await fetch(`${API_BASE}/drivers`);
        const text = await response.text();
        const driversList = text ? JSON.parse(text) : [];
        
        const container = document.querySelector('.drivers-section');
        container.innerHTML = ''; // Clear existing content

        driversList.forEach(driver => {
            drivers[driver.id] = driver;
            // Initialize availability
            if (driverAvailability[driver.id] === undefined) {
                    driverAvailability[driver.id] = driver.available !== undefined ? driver.available : true;
            }

            const cardHtml = `
        <div class="driver-card" id="driver${driver.id}Card">
            <div class="driver-header">
                <div class="driver-icon">
                    <i class="fas fa-user-tie"></i>
                </div>
                <div class="driver-info">
                    <h4>${driver.name}</h4>
                    <p id="vehicle${driver.id}">${driver.vehicleInfo}</p>
                </div>
            </div>
            <span class="status-badge status-available" id="status${driver.id}">可接單</span>
            <button class="btn btn-sm btn-outline-primary w-100 mt-2" id="toggleBtn${driver.id}" onclick="toggleDriverAvailability('${driver.id}')">
                <i class="fas fa-power-off"></i> <span id="toggleText${driver.id}">下線</span>
            </button>
            <div id="notification${driver.id}" style="display: none;"></div>
            <div id="bidForm${driver.id}" style="display: none;"></div>
            <div id="myBids${driver.id}"></div>
        </div>`;
            container.insertAdjacentHTML('beforeend', cardHtml);
            
            // Initial UI update for this driver
            updateDriverStatus(driver.id);
        });
    } catch (error) {
        console.error('載入司機資訊失敗:', error);
    }
}

// 建立乘車請求
async function createRideRequest() {
    const pickUpLocation = document.getElementById('pickUpLocation').value;
    const destination = document.getElementById('destination').value;
    const expectedPickUpTime = document.getElementById('expectedPickUpTime').value;

    if (!pickUpLocation || !destination || !expectedPickUpTime) {
        alert('請填寫所有欄位！');
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/request`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ pickUpLocation, destination, expectedPickUpTime })
        });
        
        const text = await response.text();
        currentRequest = text ? JSON.parse(text) : null;
        if (currentRequest) {
            document.getElementById('requestForm').style.display = 'none';
            document.getElementById('currentRideInfo').style.display = 'block';
            updateUI();
        } else {
            alert('建立請求失敗，請稍後再試！');
        }
    } catch (error) {
        console.error('建立請求失敗:', error);
        alert('建立請求失敗，請稍後再試！');
    }
}

// 取消行程
async function cancelRideRequest() {
    if (!confirm('確定要取消此行程嗎？')) return;

    try {
        await fetch(`${API_BASE}/cancel`, { method: 'POST' });
        // 立即清空當前請求和報價
        currentRequest = null;
        allBids = [];
        // 重置 UI
        resetUI();
    } catch (error) {
        console.error('取消失敗:', error);
        alert('取消失敗，請稍後再試！');
    }
}

// 司機提交報價
async function submitBid(driverId, driverNum) {
    const priceInput = document.getElementById(`bidPrice${driverNum}`);
    const price = parseInt(priceInput.value);

    if (!price || price <= 0) {
        alert('請輸入有效的報價金額！');
        return;
    }

    try {
        isInputting = false;  // 提交後恢復刷新
        await fetch(`${API_BASE}/bids/submit`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ driverId, price })
        });
        priceInput.value = '';
        await refreshCurrentRequest();
    } catch (error) {
        console.error('提交報價失敗:', error);
        isInputting = false;
    }
}

// 切換司機上線/下線狀態
async function toggleDriverAvailability(driverNum) {
    const driverId = driverNum.toString();
    const newStatus = !driverAvailability[driverId];
    
    try {
        await fetch(`${API_BASE}/driver-status`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ driverId, available: newStatus })
        });
        
        driverAvailability[driverId] = newStatus;
        
        // 如果司機下線，立即清空該司機的通知、報價表單和已提交報價
        if (!newStatus) {
            document.getElementById(`notification${driverNum}`).style.display = 'none';
            document.getElementById(`bidForm${driverNum}`).style.display = 'none';
            document.getElementById(`myBids${driverNum}`).innerHTML = '';
        }
        
        updateDriverStatus(driverNum);
        await refreshCurrentRequest();
    } catch (error) {
        console.error('切換狀態失敗:', error);
    }
}

// 更新司機狀態顯示
function updateDriverStatus(driverNum) {
    const driverId = driverNum.toString();
    const statusEl = document.getElementById(`status${driverNum}`);
    const toggleTextEl = document.getElementById(`toggleText${driverNum}`);
    const toggleBtnEl = document.getElementById(`toggleBtn${driverNum}`);
    
    if (driverAvailability[driverId]) {
        statusEl.className = 'status-badge status-available';
        statusEl.textContent = '可接單';
        toggleTextEl.textContent = '下線';
        toggleBtnEl.className = 'btn btn-sm btn-outline-danger w-100 mt-2';
    } else {
        statusEl.className = 'status-badge status-busy';
        statusEl.textContent = '已下線';
        toggleTextEl.textContent = '上線';
        toggleBtnEl.className = 'btn btn-sm btn-outline-success w-100 mt-2';
    }
}

// 乘客選擇報價
async function selectBid(bidId) {
    if (!confirm('確定選擇此報價嗎？選擇後將無法撤回！')) return;

    try {
        await fetch(`${API_BASE}/bids/select`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ bidId })
        });
        await refreshCurrentRequest();
    } catch (error) {
        console.error('選擇報價失敗:', error);
    }
}

// 刷新當前請求
async function refreshCurrentRequest() {
    try {
        const response = await fetch(`${API_BASE}/current`);
        
        // 檢查響應是否有內容
        const text = await response.text();
        const data = text ? JSON.parse(text) : null;
        
        if (data && data.id) {
            currentRequest = data;
            const bidsResponse = await fetch(`${API_BASE}/bids`);
            const bidsText = await bidsResponse.text();
            allBids = bidsText ? JSON.parse(bidsText) : [];
            updateUI();
        } else {
            // 只有在之前有請求的情況下才重置UI
            if (currentRequest !== null) {
                currentRequest = null;
                allBids = [];
                resetUI();
            }
        }
    } catch (error) {
        console.error('刷新失敗:', error);
    }
}

// 更新UI
function updateUI() {
    if (!currentRequest || !currentRequest.id) {
        resetUI();
        return;
    }

    // 更新乘客介面
    document.getElementById('requestForm').style.display = 'none';
    document.getElementById('currentRideInfo').style.display = 'block';
    document.getElementById('currentPickUp').textContent = currentRequest.pickUpLocation;
    document.getElementById('currentDestination').textContent = currentRequest.destination;
    document.getElementById('currentTime').textContent = currentRequest.expectedPickUpTime;
    
    const statusEl = document.getElementById('currentStatus');
    const cancelButton = document.getElementById('cancelButton');
    let statusClass = 'status-initiate';
    let statusText = '等待報價';
    
    if (currentRequest.status === 'MATCHED') {
        statusClass = 'status-matched';
        statusText = '已配對';
        document.getElementById('bidsSection').style.display = 'none';
        cancelButton.style.display = 'none';  // 配對成功後隱藏取消按鈕
        showMatchedInfo();
    } else if (currentRequest.status === 'CANCELLED') {
        statusClass = 'status-cancelled';
        statusText = '已取消';
        cancelButton.style.display = 'none';
    } else {
        cancelButton.style.display = 'block';  // INITIATE 狀態顯示取消按鈕
    }
    
    statusEl.innerHTML = `<span class="status-badge ${statusClass}">${statusText}</span>`;

    // 更新報價列表
    if (currentRequest.status === 'INITIATE') {
        updateBidsList();
    }

    // 更新司機介面
    Object.keys(drivers).forEach(driverId => {
        updateDriverUI(driverId);
    });
}

// 更新報價列表
function updateBidsList() {
    const bidsList = document.getElementById('bidsList');
    const pendingBids = allBids.filter(bid => bid.pending);
    
    if (pendingBids.length === 0) {
        bidsList.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-inbox"></i>
                <p>尚無司機報價</p>
            </div>
        `;
        return;
    }

    bidsList.innerHTML = pendingBids.map(bid => {
        const driver = bid.driver || drivers[bid.driverId] || {};
        const name = driver.name || 'Unknown Driver';
        const vehicle = driver.vehicleInfo || 'Unknown Vehicle';
        const phone = driver.phoneNumber || 'Unknown';
        
        return `
            <div class="bid-card">
                <div class="bid-header">
                    <span class="bid-driver">${name}</span>
                    <span class="bid-price">NT$ ${bid.price}</span>
                </div>
                <div class="bid-details">
                    <i class="fas fa-car"></i> ${vehicle}
                </div>
                <div class="bid-details">
                    <i class="fas fa-phone"></i> ${phone}
                </div>
                <button class="btn btn-success btn-sm w-100 mt-2" onclick="selectBid('${bid.id}')">
                    <i class="fas fa-check"></i> 選擇此司機
                </button>
            </div>
        `;
    }).join('');
}

// 顯示配對完成資訊
async function showMatchedInfo() {
    try {
        const response = await fetch(`${API_BASE}/contact-info`);
        const text = await response.text();
        const data = text ? JSON.parse(text) : null;
        if (!data) return;
        
        const selectedBid = currentRequest.selectedBid;
        const driver = selectedBid.driver || {};
        const name = driver.name || 'Unknown Driver';
        const vehicle = driver.vehicleInfo || 'Unknown Vehicle';
        const phone = driver.phoneNumber || 'Unknown';
        
        document.getElementById('matchedInfo').style.display = 'block';
        document.getElementById('matchedInfo').innerHTML = `
            <div class="contact-info">
                <h4><i class="fas fa-check-circle"></i> 配對成功！</h4>
                <div class="contact-item">
                    <strong>司機姓名：</strong> ${name}
                </div>
                <div class="contact-item">
                    <strong>司機電話：</strong> ${phone}
                </div>
                <div class="contact-item">
                    <strong>車輛資訊：</strong> ${vehicle}
                </div>
                <div class="contact-item">
                    <strong>報價金額：</strong> NT$ ${selectedBid.price}
                </div>
                <p style="margin-top: 15px; opacity: 0.9;">
                    <i class="fas fa-info-circle"></i> 請聯繫司機確認行程細節
                </p>
            </div>
        `;
    } catch (error) {
        console.error('載入聯絡資訊失敗:', error);
    }
}

// 更新司機介面
function updateDriverUI(driverNum) {
    const driverId = driverNum.toString();
    const notificationEl = document.getElementById(`notification${driverNum}`);
    const bidFormEl = document.getElementById(`bidForm${driverNum}`);
    const myBidsEl = document.getElementById(`myBids${driverNum}`);
    const statusEl = document.getElementById(`status${driverNum}`);

    // 檢查司機是否在線
    const isAvailable = driverAvailability[driverId];
    
    // 如果司機下線，清空所有內容並返回
    if (!isAvailable) {
        notificationEl.style.display = 'none';
        bidFormEl.style.display = 'none';
        myBidsEl.innerHTML = '';
        updateDriverStatus(driverNum);
        return;
    }

    // 如果沒有當前請求，清空所有內容
    if (!currentRequest || !currentRequest.id) {
        notificationEl.style.display = 'none';
        bidFormEl.style.display = 'none';
        myBidsEl.innerHTML = '';
        updateDriverStatus(driverNum);
        return;
    }

    // 顯示通知（只在INITIATE狀態且司機在線時）
    if (currentRequest.status === 'INITIATE' && isAvailable) {
        notificationEl.style.display = 'block';
        notificationEl.innerHTML = `
            <div class="notification-card">
                <h5><i class="fas fa-bell"></i> 新的乘車請求</h5>
                <p><i class="fas fa-map-marker-alt"></i> ${currentRequest.pickUpLocation} → ${currentRequest.destination}</p>
                <p><i class="fas fa-clock"></i> ${currentRequest.expectedPickUpTime}</p>
            </div>
        `;

        // 檢查是否有待處理或已撤回的報價
        const myBids = allBids.filter(bid => bid.driver && bid.driver.id === driverId);
        const hasPendingBid = myBids.some(bid => bid.pending);
        
        // 只有在沒有待處理報價且司機在線時才顯示報價表單
        if (!hasPendingBid && isAvailable) {
            bidFormEl.style.display = 'block';
            bidFormEl.innerHTML = `
                <div class="bid-form">
                    <label class="form-label">我的報價（NT$）</label>
                    <div class="input-group mb-2">
                        <input type="number" class="form-control" id="bidPrice${driverNum}" placeholder="輸入金額" 
                            onfocus="isInputting = true" 
                            onblur="isInputting = false">
                    </div>
                    <button class="btn btn-success btn-sm w-100" onclick="submitBid('${driverId}', ${driverNum})">
                        <i class="fas fa-paper-plane"></i> 提交報價
                    </button>
                </div>
            `;
        } else if (hasPendingBid) {
            bidFormEl.style.display = 'none';
        }
    } else {
        notificationEl.style.display = 'none';
        if (currentRequest.status !== 'INITIATE') {
            bidFormEl.style.display = 'none';
        }
    }

    // 顯示我的報價
    const myBids = allBids.filter(bid => bid.driver && bid.driver.id === driverId);
    const pendingBids = myBids.filter(bid => bid.pending);
    
    // 如果請求已配對，且司機的報價未被選中，則不顯示任何內容
    if (currentRequest.status === 'MATCHED') {
        const isMyBidSelected = myBids.some(bid => 
            currentRequest.selectedBid && currentRequest.selectedBid.id === bid.id
        );
        if (!isMyBidSelected) {
            myBidsEl.innerHTML = '';
            return;
        }
    }
    
    if (myBids.length > 0) {
        myBidsEl.innerHTML = myBids.map(bid => {
            const isSelected = currentRequest.selectedBid && currentRequest.selectedBid.id === bid.id;
            
            if (isSelected) {
                // 不再變更司機狀態為「已配對」
                return `
                    <div class="bid-form" style="background: #d1f2eb; border: 2px solid var(--success-color);">
                        <h6 style="color: var(--success-color); margin-bottom: 10px;">
                            <i class="fas fa-check-circle"></i> 您的報價已被選中！
                        </h6>
                        <p style="font-size: 1.2rem; font-weight: 700; color: var(--success-color); margin: 0;">
                            NT$ ${bid.price}
                        </p>
                        <p style="margin-top: 10px; color: #6c757d; font-size: 0.9rem;">
                            乘客電話：${currentRequest.passenger.phoneNumber}
                        </p>
                    </div>
                `;
            }
            
            if (bid.pending) {
                return `
                    <div class="bid-form">
                        <h6 style="color: var(--primary-color); margin-bottom: 10px;">我的報價</h6>
                        <p style="font-size: 1.2rem; font-weight: 700; color: var(--primary-color); margin: 0;">
                            NT$ ${bid.price}
                        </p>
                        <p style="margin-top: 10px; color: #6c757d; font-size: 0.9rem;">
                            等待乘客回應...
                        </p>
                    </div>
                `;
            }
            
            return '';
        }).join('');
        
        // 如果沒有待處理報價且狀態是INITIATE，顯示報價表單（允許重新報價）
        if (pendingBids.length === 0 && currentRequest.status === 'INITIATE' && isAvailable) {
            bidFormEl.style.display = 'block';
        }
    } else {
        myBidsEl.innerHTML = '';
        if (currentRequest.status !== 'INITIATE') {
            updateDriverStatus(driverNum);
        }
    }
}

// 重置UI
function resetUI() {
    // 顯示請求表單
    document.getElementById('requestForm').style.display = 'block';
    document.getElementById('currentRideInfo').style.display = 'none';
    document.getElementById('matchedInfo').style.display = 'none';
    document.getElementById('bidsSection').style.display = 'block';
    
    // 清空輸入欄位
    document.getElementById('pickUpLocation').value = '';
    document.getElementById('destination').value = '';
    document.getElementById('expectedPickUpTime').value = '';
    
    // 清空報價列表
    document.getElementById('bidsList').innerHTML = '';
    
    // 清理所有司機介面
    Object.keys(drivers).forEach(driverId => {
        const notificationEl = document.getElementById(`notification${driverId}`);
        const bidFormEl = document.getElementById(`bidForm${driverId}`);
        const myBidsEl = document.getElementById(`myBids${driverId}`);
        
        if (notificationEl) notificationEl.style.display = 'none';
        if (bidFormEl) bidFormEl.style.display = 'none';
        if (myBidsEl) myBidsEl.innerHTML = '';
        updateDriverStatus(driverId);
    });
}

// 重置整個系統
async function resetSystem() {
    if (!confirm('確定要重置整個系統嗎？這將清除所有資料！')) return;
    
    try {
        // 如果有當前請求，先取消
        if (currentRequest) {
            await fetch(`${API_BASE}/cancel`, { method: 'POST' });
        }
        
        // 清空前端狀態
        currentRequest = null;
        allBids = [];
        
        // 重置所有司機為上線狀態
        for (const driverId of Object.keys(drivers)) {
            if (!driverAvailability[driverId]) {
                await fetch(`${API_BASE}/driver-status`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ driverId, available: true })
                });
                driverAvailability[driverId] = true;
            }
        }
        
        // 重置UI
        resetUI();
        
        alert('系統已重置！');
    } catch (error) {
        console.error('重置系統失敗:', error);
        alert('重置失敗，請稍後再試！');
    }
}

// 頁面載入時初始化
window.onload = init;