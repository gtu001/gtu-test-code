const express = require('express');

// 建立 Router 物件
const router = express.Router();
const app = express();
const PORT = process.env.PORT || 8001;
const HOST = '0.0.0.0';

// 舊方法
app.get('/sample', function(req, res) {
  res.send('this is a sample!');
});

// 在每一個請求被處理之前都會執行的 middleware
router.use((req, res) => {
  // res.json({ status: 'UP' });

  // 輸出記錄訊息至終端機
  console.log(req.method, req.url);

  // 繼續路由處理
  //next();
});

// Home page route.
router.get('/', function (req, res) {
  res.send('Wiki home page');
});

// About page route.
router.get('/about', function (req, res) {
  res.send('About this wiki');
});


// 驗證 :name 的 route middleware
router.param('name', function(req, res, next, name) {
  // 在這裡驗證資料
  // ... ... ...

  // 顯示驗證訊息
  console.log('doing name validations on ' + name);

  // 當驗證成功時，將其儲存至 req
  req.name = name;

  // 繼續後續的處理流程
  //next();
});


// 將路由套用至應用程式
app.use('/', router);
// ---- 啟動伺服器 ----
//app.listen(PORT, HOST);
app.listen(PORT);

module.exports = router;
module.exports = app;

console.log(`Running on http://${HOST}:${PORT}`);
