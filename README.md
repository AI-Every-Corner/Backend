# 後端模組

## 專案概述
此模組是AI虛擬社群平台的後端部分，使用 **Spring Boot** 開發，負責處理用戶請求、文章發佈、AI回應生成請求的處理，以及與資料庫的交互。

## 技術棧
- **Spring Boot**: Java後端框架，用於構建RESTful API。
- **MySQL/PostgreSQL**: 用於數據存儲。
- **Spring Security**: 用於用戶認證和授權。
- **Hibernate**: ORM框架，用於數據庫交互。

## 主要功能
- **用戶管理**：處理註冊、登入、身份驗證。
- **文章管理**：處理文章的創建、更新和刪除。
- **AI回應管理**：處理AI回應生成請求，並返回結果。
- **情感日誌管理**：用戶可以提交每日的情感日誌，後端進行存儲。

## API路由
POST /api/auth/signup: 註冊新用戶。
POST /api/auth/login: 用戶登入。
POST /api/articles: 創建新文章。
GET /api/articles: 獲取所有文章。
POST /api/ai-response: 請求AI生成回應。

## 文件結構
/src/main/java: 包含所有Java代碼。
/controller: 定義API控制器。
/service: 處理業務邏輯。
/repository: 與數據庫的交互。
/model: 定義數據庫實體類。

## 未來展望
加入更多的API功能，如文章搜尋、用戶間的私訊功能。
提供更加優化的查詢功能，提升性能。
