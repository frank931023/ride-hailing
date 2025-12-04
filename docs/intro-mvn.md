# Maven 與 .mvn 資料夾介紹

## 什麼是 Maven？

**Apache Maven** 是一個主要用於 Java 專案的**專案管理與自動化建置工具**。它基於專案物件模型（Project Object Model，簡稱 POM）的概念，透過一個核心的設定檔 `pom.xml` 來管理專案的建置、報告和文件。

### Maven 的核心功能：

1.  **依賴管理 (Dependency Management)**：
    這是 Maven 最強大的功能之一。您只需要在 `pom.xml` 中宣告您需要的函式庫（例如 Spring Boot, JUnit），Maven 就會自動從中央儲存庫（Maven Central Repository）下載這些 jar 檔及其依賴的其他 jar 檔，免去了手動管理 classpath 的痛苦。

2.  **標準化的目錄結構**：
    Maven 提倡 "Convention over Configuration"（慣例優於配置）。它定義了一套標準的專案結構（如 `src/main/java` 放原始碼，`src/test/java` 放測試碼），讓任何熟悉 Maven 的開發者都能快速看懂您的專案結構。

3.  **建置生命週期 (Build Lifecycle)**：
    Maven 定義了標準的建置階段，例如：
    - `clean`: 清除上次建置產生的檔案。
    - `compile`: 編譯原始碼。
    - `test`: 執行單元測試。
    - `package`: 將編譯後的程式碼打包成可發布的格式（如 JAR 或 WAR）。
    - `install`: 將打包好的檔案安裝到本地儲存庫，供其他本地專案使用。

---

## 什麼是 .mvn 資料夾？

`.mvn` 資料夾是 **Maven Wrapper** 機制的配置目錄。它的主要目的是確保專案可以在任何環境下（開發者的電腦、CI/CD 伺服器等）使用**特定版本**的 Maven 進行建置，而不需要預先手動安裝 Maven。

## 資料夾結構與內容

在 `.mvn` 資料夾中，通常包含以下內容：

- **wrapper/**
  - `maven-wrapper.properties`: 這是核心設定檔。它指定了該專案所需的 Maven 版本（例如 3.8.6）以及下載 Maven 的 URL。
  - `maven-wrapper.jar` (有時會存在，或由 script 下載): 負責實際下載並執行 Maven 的 Java 程式庫。

## 它是如何運作的？

當您在專案根目錄看到 `mvnw` (Linux/macOS) 和 `mvnw.cmd` (Windows) 這兩個腳本時，它們就是搭配 `.mvn` 資料夾運作的。

1. 當您執行 `./mvnw clean install` 時，腳本會讀取 `.mvn/wrapper/maven-wrapper.properties`。
2. 它會檢查您的電腦是否已經下載了指定版本的 Maven。
3. 如果沒有，它會自動下載該版本的 Maven 到使用者的家目錄 (`~/.m2/wrapper/`)。
4. 最後，它會使用這個下載的 Maven 來執行您的建置命令。

## 為什麼要保留它？

保留 `.mvn` 資料夾與 `mvnw` 腳本有以下好處：

1. **環境一致性**：確保所有開發者和 CI 伺服器都使用完全相同的 Maven 版本，避免因為版本差異導致的建置錯誤 ("It works on my machine" 問題)。
2. **快速上手**：新加入的開發者不需要安裝 Maven，只要有 Java 環境，拉下程式碼後直接執行 `./mvnw` 即可開始工作。

## 總結

`.mvn` 資料夾是專案建置工具鏈的重要組成部分，**不建議刪除**。它讓您的專案更加獨立且易於移植。
