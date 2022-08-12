select merchantName, hashedShopperReference, averageRiskScore, totalNumberOfTransactions
FROM 
Merchant as merch 
join
(select hashedShopperReference, merchantId, round(avg(riskScore)*1.00, 2) as averageRiskScore, count(transactionId) as totalNumberOfTransactions
 FROM 'Transaction'
 group by merchantId, hashedShopperReference) as transInfo
 ON transInfo.merchantId = merch.merchantId
 WHERE averageRiskScore >= 100.00 
 order by merchantName ASC, averageRiskScore DESC;
 
