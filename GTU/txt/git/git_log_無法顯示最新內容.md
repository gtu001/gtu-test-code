出現錯誤
(unable to update local ref)


輸入
 $ git gc --prune=now
或
 $ git remote prune origin  <--- 會砍掉所有異動內容, 記得backup


在顯示log 顯示沒有任何 log 
要重新 checkout branch
 
 $ git checkout -f --no-track -b <本地branch> <遠端branch> --
 Ex :
 $ git checkout -f --no-track -b feature/#NCSPOS-T028 remotes/origin/Feature/#NCSPOS-T028 --
