反向肯定預查
---
Ex : 
	(?<=95|98|NT|2000)Windows
	
	開頭為 95|98|NT|2000 捕捉 Windows



反向否定預查
---

Ex : 
	(?<!95|98|NT|2000)Windows
	
	開頭為 95|98|NT|2000 不捕捉 Windows
	
	
	
正向肯定預查
---
Ex : 
	Windows(?=95|98|NT|2000)
	
	結尾為 95|98|NT|2000 捕捉Windows 



正向否定預查
---

Ex : 
	Windows(?!95|98|NT|2000)
	
	結尾為 95|98|NT|2000 不捕捉 Windows
	
	

正向不補捉
---

Ex :
	industr(?:y|ies)
		
	可同時匹配 industry , industries
	
