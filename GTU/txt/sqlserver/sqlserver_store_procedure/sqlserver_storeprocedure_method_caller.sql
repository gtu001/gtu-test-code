

 declare @TrustAcct varchar(12)
 declare @TrustCapital money
 declare @MonDrawDate1 int
 declare @MonDrawDate2 int
 declare @MonDrawDate3 int
 declare @CapitalN money --OUTPUT
 declare @DayN int --OUTPUT
 declare @ApproveDate char(8)--OUTPUT
 declare  @Capital1 money  --OUTPUT
 declare @Day1 int  --OUTPUT
 declare  @AppID1 char(13) --OUTPUT
 declare @Capital2 money  --OUTPUT
 declare @Day2 int  --OUTPUT
 declare @AppID2 char(13) --OUTPUT
 declare @MonDrawDate11 int  --OUTPUT
 declare @MonDrawDate12 int  --OUTPUT
 declare @MonDrawDate13 int --OUTPUT
 declare @MonDrawDate21 int  --OUTPUT
 declare @MonDrawDate22 int  --OUTPUT
 declare @MonDrawDate23 int --OUTPUT
 declare @ret int

 set @TrustAcct ='00178135572'
 --set @TrustCapital ='0'

  exec @ret = CTF.dbo.GetTAOffShelvev1  @TrustAcct ,  @TrustCapital ,  @MonDrawDate1 , @MonDrawDate2 ,  @MonDrawDate3 , @CapitalN output, @DayN output
         , @ApproveDate output, @Capital1 output, @Day1 output, @AppID1 output, @Capital2 output, @Day2 output, @AppID2 output, @MonDrawDate11 output, @MonDrawDate12 output, @MonDrawDate13 output, @MonDrawDate21 output, @MonDrawDate22 output, @MonDrawDate23 output

  select @ret as ret, @CapitalN as CapitalN, @DayN as DayN, @ApproveDate as ApproveDate, @Capital1 as Capital1, @Day1 as Day1, @AppID1 as AppID1, @Capital2 as Capital2, @Day2 as Day2, @AppID2 as AppID2, @MonDrawDate11 as MonDrawDate11, @MonDrawDate12 as MonDrawDate12, @MonDrawDate13 as MonDrawDate13, @MonDrawDate21 as MonDrawDate21, @MonDrawDate22 as MonDrawDate22, @MonDrawDate23 as MonDrawDate23


