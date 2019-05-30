USE [CTF]
GO

/****** Object:  View [dbo].[PeriodVarTJ]    Script Date: 2019/5/28 上午 09:41:15 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE view [dbo].[PeriodVarTJ] as  
select distinct PVL.YYYYMMDD  
, PVL.TrustCapital, PVL.FeeB + PVL.FeeT + PVL.FeeI + PVL.Fee4 Fee  
, PVL.TrustCapitalV, PVL.FeeBV + PVL.FeeTV + PVL.FeeIV + PVL.Fee4V FeeV  
, PVL.PricePct, PVL.PriceDir, PVP.PriceW, PVP.PriceY, PVP.PriceWYPct  
, PVL.TrustAcct, PVL.MonDrawDate, PVP.VarDay, PVP.FundID  
, TJ.OrderDate, isnull(PPD.CustMobilePhone, '') CustMobilePhone  
from TrustAcct TA  
, (select YYYYMM, FundID, VarDay, PriceW, PriceM, PriceY, PriceWYPct from CTF.dbo.PeriodVarPrice   
   union all  
   select YYYYMM, FundID, VarDay, PriceW, PriceM, PriceY, PriceWYPct from CTF.dbo.PeriodVarPriceLog) PVP  
, (select * from PeriodVarLog union all select * from PeriodVarLogHis) PVL  
left outer join TradeJournal TJ ON TJ.DealType = 1 and TJ.TrustType = 1 and TJ.TrustAcct = PVL.TrustAcct and TJ.OrderDate = PVL.YYYYMMDD  
left outer join (  
select TrustAcct, CustMobilePhone, OrderDate, MonDrawDate from PeriodPhoneData  
union all  
select TrustAcct, CustMobilePhone, OrderDate, MonDrawDate from PeriodPhoneDataHis  
) PPD on PPD.TrustAcct = PVL.TrustAcct and PPD.OrderDate = PVL.YYYYMMDD  
where PVL.TrustAcct = TA.TrustAcct   
and TA.FundID = PVP.FundID  
and case when right(cast(PVL.YYYYMMDD as varchar), 2) < right('00' + cast(PVL.MonDrawDate as varchar), 2)  
    then left(convert(varchar, dateadd(m, -1, cast(PVL.YYYYMMDD as varchar)), 112), 6) + right('00' + cast(PVL.MonDrawDate as varchar), 2)  
    else left(cast(PVL.YYYYMMDD as varchar), 6) + right('00' + cast(PVL.MonDrawDate as varchar), 2) end  
    = cast(PVP.YYYYMM as varchar) + right('00' + cast(PVP.VarDay as varchar), 2)  
  
GO


