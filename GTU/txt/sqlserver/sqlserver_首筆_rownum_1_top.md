取得至頂幾筆 TOP(n)
---
        SELECT  TOP(999)  c.id as companyId,
				bm.UniformNo as CompanyCode,
				bm.UniformNo as UniformNo,
				...
		FROM zt_customer c
		left join zt_billerMain bm on bm.UniformNo = c.code
		where  (GETDATE() > bm.EffectDate) and (bm.Status = 1)
                                        [ and c.id = :companyId ] 
                                        [ and bm.BillerCode = :billerCode ] 
                                        [ and bm.BillerCode = :billerInfoCode ] 
                                        [ and bm.TurnFile = :turnFile ] 
                order by bm.EffectDate desc