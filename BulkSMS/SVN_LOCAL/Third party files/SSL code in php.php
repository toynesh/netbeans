<?php

date_default_timezone_set('Africa/Nairobi');

$SERVICEID = "11111";
$spId = '11111';
//$password = 'Joh5684';
$password = '!Qwsx34';
$timestamp_ = date("YdmHis");
$real_pass = base64_encode(hash('sha256', $spId . "" . $password . "" . $timestamp_));

$rand = rand(123456, 654321);
$originId = $spId . "_Beverly_" . $rand;

$reqTime = date('Y-m-d') . "T" . date('H:i:s') . ".0000521Z"; //2014-10-21T09:47:19.0000521Z

$curlData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://api-v1.gen.mm.vodafone.com/mminterface/request\" xmlns:ns2=\"http://www.huawei.com.cn/schema/common/v2_1\">
    <soapenv:Header>
        <ns2:RequestSOAPHeader>
            <ns2:spId>$spId</ns2:spId>
            <ns2:spPassword>$real_pass</ns2:spPassword>
            <ns2:serviceId>$SERVICEID</ns2:serviceId>
            <ns2:timeStamp>$timestamp_</ns2:timeStamp>
        </ns2:RequestSOAPHeader>
    </soapenv:Header>
    <soapenv:Body>
        <ns1:RequestMsg><![CDATA[<?xml version='1.0' encoding='UTF-8'?><request xmlns='http://api-v1.gen.mm.vodafone.com/mminterface/request'>
<Transaction>
 <CommandID>SalaryPayment</CommandID>
 <LanguageCode>0</LanguageCode>
 <OriginatorConversationID>$originId</OriginatorConversationID>
 <ConversationID></ConversationID>
 <Remark>0</Remark>
 <EncryptedParameters></EncryptedParameters>
<Parameters><Parameter>
 <Key>Amount</Key>
 <Value>500</Value>
</Parameter></Parameters>
<ReferenceData>
 <ReferenceItem>
  <Key>QueueTimeoutURL</Key>
  <Value>http://41.78.27.222/sdp_apps/services/b2c/queueTimeout.php</Value>
 </ReferenceItem></ReferenceData>
 <Timestamp>$reqTime</Timestamp>
</Transaction>
<Identity>
 <Caller>
  <CallerType>2</CallerType>
  <ThirdPartyID>$spId</ThirdPartyID>
  <Password>Password0</Password>
  <CheckSum>CheckSum0</CheckSum>
  <ResultURL>http://41.78.27.222/sdp_apps/services/b2c/resultMsg.php</ResultURL>
 </Caller>
 <Initiator>
  <IdentifierType>11</IdentifierType>
  <Identifier>beverly_Init</Identifier>
  <SecurityCredential>v5mYKHNVy+8FLRyLLdxmavgGF5MpNIbajP1MZtN2aPGGdvZL/x9ILYHCfFdE48r8vPbIZPZnMh2JQuLsz+ihOR+guXAdnR9RIdT8FUYRSwKBqHM0fM77WC9V4MTR1RjQ/AliOS/S4eFXZQbRLvWAcRY3aX3nhjTtNLeF1HrrWNxudJM8riq9GeWGGnUsy2Z4TC6vwO2FZiIR+qtqG+tbB8P+X7oo9mOTrqgGuCYTGoudj8aU4n7SZwhjOJbyhkTj5ibel91mDTrcOs4CeowKGKDQstTPD/wjyBO1RcjIA5R/ZunXgcx4XmfQcKYMgGDA0zKzUqKeZqMBISypC0Q7Ew==</SecurityCredential>
  <ShortCode>$spId</ShortCode>
 </Initiator>
  <PrimaryParty>
   <IdentifierType>4</IdentifierType>
   <Identifier>$spId</Identifier>
   <ShortCode></ShortCode>
  </PrimaryParty>
 <ReceiverParty>
  <IdentifierType>1</IdentifierType>
  <Identifier>254726770792</Identifier>
  <ShortCode></ShortCode>
 </ReceiverParty>
 <AccessDevice>
  <IdentifierType>1</IdentifierType>
  <Identifier>Identifier3</Identifier>
  </AccessDevice></Identity>
  <KeyOwner>1</KeyOwner>
 </request>]]></ns1:RequestMsg>
    </soapenv:Body></soapenv:Envelope>";

//echo date('Y-m-d H:i:s') . ': Request: ' . $curlData . "\n\n";
//$url = 'http://196.201.214.136:8310/mminterface/request';
//$url = 'https://196.201.214.136:18423/mminterface/request';
$url = "https://196.201.214.136:18423/mminterface/request";
$curl = curl_init();

curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($curl, CURLOPT_TIMEOUT, 120);
curl_setopt($curl, CURLOPT_CONNECTTIMEOUT, 120);
curl_setopt($curl, CURLOPT_ENCODING, 'utf-8');

curl_setopt($curl, CURLOPT_HTTPHEADER, array(
    'SOAPAction:""',
    'Content-Type: text/xml; charset=utf-8',
));
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, 2);
curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, FALSE);

curl_setopt($curl, CURLOPT_USERAGENT, 'Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)');
//CURLOPT_VERBOSE        => true,
//curl_setopt($curl, CURLOPT_SSLCERT, '/var/www/html/sdp_apps/services/b2c/pkcs/testbroker.crt');
curl_setopt($curl, CURLOPT_SSLCERT, '/var/www/html/sdp_apps/services/b2c/pkcs/certs_chain.pem');
curl_setopt($curl, CURLOPT_SSLCERTTYPE, 'PEM');

//curl_setopt($curl, CURLOPT_SSLKEY, '/var/www/html/sdp_apps/services/b2c/pkcs/certs_chain.pem');
curl_setopt($curl, CURLOPT_SSLKEY, '/etc/httpd/ssl/beverly.key');
curl_setopt($curl, CURLOPT_SSLKEYPASSWD, '');

curl_setopt($curl, CURLOPT_POST, 1);
curl_setopt($curl, CURLOPT_POSTFIELDS, $curlData);
curl_setopt($curl, CURLOPT_HEADERFUNCTION, 'read_header'); // get header

$result = curl_exec($curl);
if (curl_errno($curl)) {
    echo 'Curl Error: ' . curl_error($curl) . "\n\n";
}

echo date('Y-m-d H:i:s') . ": Response: $result\n";
curl_close($curl);

//$xml = new SimpleXMLElement($result);
//print_r($xml);

function read_header($curl, $string) {
    print "Received header: $string\n\n";
    return strlen($string);
}

?>