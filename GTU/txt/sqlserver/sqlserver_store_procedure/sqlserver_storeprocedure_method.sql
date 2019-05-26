USE [CTF]
GO

/****** Object:  StoredProcedure [dbo].[GetTAOffShelvev1]    Script Date: 2019/4/24 上午 11:25:38 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[GetTAOffShelvev1]
  @TrustAcct varchar(12),
  @TrustCapital money,
  @MonDrawDate1 int,
  @MonDrawDate2 int,
  @MonDrawDate3 int,
  @CapitalN money OUTPUT,
  @DayN int OUTPUT,
  @ApproveDate char(8) OUTPUT,
  @Capital1 money OUTPUT, 
  @Day1 int OUTPUT, 
  @AppID1 char(13) OUTPUT,
  @Capital2 money OUTPUT, 
  @Day2 int OUTPUT, 
  @AppID2 char(13) OUTPUT,
  @MonDrawDate11 int OUTPUT, 
  @MonDrawDate12 int OUTPUT, 
  @MonDrawDate13 int OUTPUT,
  @MonDrawDate21 int OUTPUT, 
  @MonDrawDate22 int OUTPUT, 
  @MonDrawDate23 int OUTPUT
AS
SET NOCOUNT ON
begin

Declare @FundID char(8)
Declare @Capital0 money, @Day0 int, @AppID0 char(13), @TxCTF int, @CustID varchar(11)

  select @FundID = FundID, @TxCTF = TxCTF, @CustID = CustomerID from TrustAcctCTF with (nolock) where TrustAcct = @TrustAcct
  if @TxCTF in (0, 2)
  begin
    select @ApproveDate = rtrim(ApproveDate) from CTF.dbo.Fund with (nolock) where FundID = @FundID and ApproveFlg = 'N' And StopPurchFlg = '101'
  end
  else
  begin
    select @ApproveDate = rtrim(ApproveDate) from CTFL.dbo.Fund with (nolock) where FundID = @FundID and ApproveFlg = 'N' And StopPurchFlg = '101'
  end    
  select @Day0 = len(replace(cast(@MonDrawDate1 as varchar) + cast(@MonDrawDate2 as varchar)+ cast(@MonDrawDate3 as varchar), '0', '')) 
  select @Capital0 = @TrustCapital
  
  if len(@ApproveDate) = 8 
  begin
    if @TxCTF in (0, 2)
    begin
        select @AppID1 = max(ApplicationID) 
        from CTF.dbo.Application with (nolock) 
        where TrustAcct = @TrustAcct and DealType in (0,1) and ApplicationDate <= @ApproveDate and CustomerID = @CustID
          and ApplicationDate in (select max(ApplicationDate) from CTF.dbo.Application with (nolock) where TrustAcct = @TrustAcct and DealType in (0,1) and ApplicationDate <= @ApproveDate and CustomerID = @CustID)
        if len(@AppID1) = 13 
        begin
          select @Capital1 = TrustCapital, @Day1 = len(replace(cast(MonDrawDate1 as varchar) + cast(MonDrawDate2 as varchar)+ cast(MonDrawDate3 as varchar), '0', '')) 
                 , @MonDrawDate11 = MonDrawDate1, @MonDrawDate12 = MonDrawDate2, @MonDrawDate13 = MonDrawDate3
          from CTF.dbo.Application with (nolock) where ApplicationID = @AppID1 and CustomerID = @CustID
          select @CapitalN = @Capital1, @DayN = @Day1
        end              
        select @AppID2 = max(ApplicationID) 
        from CTF.dbo.Application with (nolock) 
        where TrustAcct = @TrustAcct and DealType in (0,1) and ApplicationDate > '20120316' and CustomerID = @CustID
          and ApplicationDate in (select max(ApplicationDate) from CTF.dbo.Application with (nolock) where TrustAcct = @TrustAcct and DealType in (0,1) and ApplicationDate > '20120316' and CustomerID = @CustID)
        if len(@AppID2) = 13 
        begin
          select @Capital2 = TrustCapital, @Day2 = len(replace(cast(MonDrawDate1 as varchar) + cast(MonDrawDate2 as varchar)+ cast(MonDrawDate3 as varchar), '0', '')) 
                 , @MonDrawDate21 = MonDrawDate1, @MonDrawDate22 = MonDrawDate2, @MonDrawDate23 = MonDrawDate3
          from CTF.dbo.Application with (nolock) where ApplicationID = @AppID2 and CustomerID = @CustID
          select @CapitalN = @Capital2, @DayN = @Day2
        end              
    end        
    else
    begin
        select @AppID1 = max(ApplicationID) 
        from CTFL.dbo.Application with (nolock) 
        where TrustAcct = @TrustAcct and DealType in (0,1) and ApplicationDate <= @ApproveDate and CustomerID = @CustID
        and ApplicationDate in (select max(ApplicationDate) from CTFL.dbo.Application with (nolock) where TrustAcct = @TrustAcct and DealType in (0,1) and ApplicationDate <= @ApproveDate and CustomerID = @CustID)
        if len(@AppID1) = 13 
        begin
          select @Capital1 = TrustCapital, @Day1 = len(replace(cast(MonDrawDate1 as varchar) + cast(MonDrawDate2 as varchar)+ cast(MonDrawDate3 as varchar), '0', '')) 
                 , @MonDrawDate11 = MonDrawDate1, @MonDrawDate12 = MonDrawDate2, @MonDrawDate13 = MonDrawDate3
          from CTFL.dbo.Application with (nolock) where ApplicationID = @AppID1 and CustomerID = @CustID
          select @CapitalN = @Capital1, @DayN = @Day1
        end              
        select @AppID2 = max(ApplicationID) 
        from CTFL.dbo.Application 
        where TrustAcct = @TrustAcct and DealType in (0,1) and ApplicationDate > '20120316' and CustomerID = @CustID
        and ApplicationDate in (select max(ApplicationDate) from CTFL.dbo.Application where TrustAcct = @TrustAcct and DealType in (0,1) and ApplicationDate > '20120316' and CustomerID = @CustID)
        if len(@AppID2) = 13 
        begin
          select @Capital2 = TrustCapital, @Day2 = len(replace(cast(MonDrawDate1 as varchar) + cast(MonDrawDate2 as varchar)+ cast(MonDrawDate3 as varchar), '0', '')) 
                 , @MonDrawDate21 = MonDrawDate1, @MonDrawDate22 = MonDrawDate2, @MonDrawDate23 = MonDrawDate3
          from CTFL.dbo.Application with (nolock) where ApplicationID = @AppID2 and CustomerID = @CustID
          select @CapitalN = @Capital2, @DayN = @Day2
        end              
    end    
    if @CapitalN >= @Capital0 and @DayN >= @Day0 
      return 1
    else
      return 0
  end
  return 1
end    






GO


