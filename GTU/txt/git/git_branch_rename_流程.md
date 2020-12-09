git_branch_rename_流程.md

	git checkout <old_name>
	git branch -m <new_name>
	git push origin -u <new_name>
	git push origin --delete <old_name>