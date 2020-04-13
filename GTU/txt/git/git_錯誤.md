error: cannot lock ref 'refs/remotes/origin/xxx': ref xxx is at OOO expected XXX

Ex : 
error: cannot lock ref 'refs/remotes/origin/feature/#NCSPOS-71': is at dce8121320231131602cc7eb4e5698da0956a6dc but expected c3c6618e06b792f778db32faf95d6075976a1e55


	方案一、cmd指令:(專案資料夾右鍵點選Git Bash Here)
	  git gc --prune=now
	  git remote prune origin

	方案二、至專案中".git"隱藏資料夾手動刪除問題分支
	   X:\xxx\.git\refs\remotes\origin\branchname