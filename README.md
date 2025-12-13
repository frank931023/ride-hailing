# 「系統分析與設計」課程作業 -- 簡易乘車配對系統 (Ride-Hailing System)

- 這是一個基於 Java Spring Boot 開發的簡易乘車配對平台，模擬了乘客發送乘車請求、司機報價以及乘客選擇司機進行配對的完整流程。

## 👥 組員資訊
- 資管三 112403531 汪筠涵
- 資管三 112403026 魏仁祥
- 資管三 112403523 傅聖祐
- 資管三 112403525 黃媺涵
- 資管三 112403543 王暐元


## 系統簡介

系統架構前後端分離，旨在展示一個簡易的叫車服務架構 (前端為 GenAI 生成之單一 HTML 檔案)。
- **乘客**：可以輸入上車地點、目的地與時間，發送請求，並查看司機的即時報價。
- **司機**：可以查看線上的乘車請求並提交報價。
- **配對**：乘客從報價列表中選擇心儀的司機，完成配對並交換聯絡資訊。

---

## 安裝與啟動教學

### 環境需求
- **Docker** (推薦)
- 或 **Java JDK 17+** 與 **Maven**

### 方法一：Docker

1. 開啟終端機 (Terminal/PowerShell)。
2. 進入專案根目錄。
3. 執行以下指令：
   ```bash
   docker compose up --build
   ```
4. 等待看到 `Started RideHailingApplication` 字樣。
5. 開啟瀏覽器訪問：[http://localhost:8080](http://localhost:8080)

---

### 方法二：使用 Maven 本機執行

1. 確保已安裝 JDK 17 或以上版本。
2. 在專案根目錄執行：
   - **Windows**:
     ```powershell
     .\mvnw spring-boot:run
     ```
   - **Mac/Linux**:
     ```bash
     ./mvnw spring-boot:run
     ```
3. 啟動後訪問：[http://localhost:8080](http://localhost:8080)

## 專案結構說明

採用標準的 MVC 分層架構，主要程式碼位於 `src/main/java/com/example/ride_hailing/` 下。

### 1. Controller 層 (`controller/`)
負責接收前端的 HTTP 請求，並將請求轉發給 Service 層處理。
- **`RideController.java`**: 
  - 定義了所有的 API 端點 (Endpoints)，如 `/api/rides/request` (建立請求), `/api/rides/bids` (取得報價)。
  - 作為前端與後端邏輯的入口點。

### 2. Service 層 (`service/`)
負責業務邏輯的處理與狀態管理。
- **`RideService.java`**: 
  - 系統的核心服務類別。
  - 管理司機列表 (`drivers`)、當前的乘客 (`currentPassenger`) 與乘車請求 (`currentRideRequest`)。
  - 協調 Model 層的物件進行運算（例如呼叫 `Passenger` 物件來獲取報價列表）。

### 3. Model 層 (`model/`)
定義系統的資料結構與核心商業邏輯。
- **`Passenger.java`**: 
  - 代表乘客實體。
  - **關鍵邏輯**：包含 `updateBideList` 方法，負責決定乘客能看到哪些報價（這是系統設計的一個特點，報價列表是由乘客物件視角提供的）。
- **`Driver.java`**: 代表司機實體，包含車輛資訊與狀態。
- **`RideRequest.java`**: 代表一筆乘車請求，包含起訖點、狀態 (`INITIATE`, `MATCHED`) 與收到的報價列表。
- **`Bid.java`**: 代表司機對某一請求的報價。
- **`MatchService.java`**: 處理具體的配對邏輯，如建立請求、新增報價、執行配對等。

### 4. 前端資源 (`src/main/resources/static/`)
- **`index.html`**: 使用者介面，包含乘客端與司機端的模擬畫面。
- **`script.js`**: 前端邏輯，負責透過 `fetch` API 與後端 Controller 溝通，並動態更新頁面 DOM 元素。

---

## 系統交互流程

以下說明一個完整的叫車流程是如何在各個檔案間交互運作的：

1.  **發送請求**：
    - **前端** (`script.js`) 呼叫 `POST /api/rides/request`。
    - **Controller** (`RideController`) 接收請求並呼叫 `RideService.createRideRequest`。
    - **Service** 透過 `MatchService` 建立一個新的 `RideRequest` 物件。

2.  **司機報價**：
    - **前端** (司機介面) 呼叫 `POST /api/rides/bids/submit`。
    - **Controller** 接收並轉發給 `RideService`。
    - **Service** 將報價 (`Bid`) 加入到該 `RideRequest` 的報價列表中。

3.  **顯示報價列表 (關鍵交互)**：
    - **前端** 定時輪詢 `GET /api/rides/bids`。
    - **Controller** 呼叫 `RideService.getAllBids()`。
    - **Service** 呼叫 `currentPassenger.updateBideList(currentRideRequest)`。
    - **Model** (`Passenger`) 執行邏輯並回傳報價列表給 Service -> Controller -> 前端。
    - *這確保了資料是經過乘客物件的邏輯過濾或處理的。*

4.  **選擇配對**：
    - **前端** (乘客介面) 呼叫 `POST /api/rides/bids/select`。
    - **Service** 更新請求狀態為 `MATCHED`。
    - **前端** 顯示配對成功資訊。

---
