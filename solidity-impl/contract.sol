pragma solidity ^0.4.0;
contract EncryptedToken{

    struct Trade{
        address emmiter;
        address receiver;
        bytes30[]amount;
        
    }
    uint256 nonce;
    uint verificationNum;
    mapping(address => bool)verifiers;
    mapping(address => bytes30[]) balances;
    mapping(address => bytes30[]) newbalances;
    mapping(address=>uint) checks;
    mapping(uint256 => Trade) txpool;
    mapping(uint256 => bool) status;
    mapping(address => uint256)nonces;
    mapping(address => bool) isActivated;
    function Ballot(address[] verifiersInit,bytes30[]initAmount) public {
        for (uint i =0;i<verifiersInit.length;i++){
            verifiers[verifiersInit[i]]=true;
        }
        verificationNum=verifiersInit.length;
        balances[msg.sender]=initAmount;
        nonce=0;
    }

    function patch(address x, address y, bytes30[] datax,bytes30[]datay) {
        if (!verifiers[msg.sender]) return;
        newbalances[x]=datax;
        newbalances[y]=datay;
        checks[x]=1;
        checks[y]=1;
    }
    function validate(address x,address y){
        if (!verifiers[msg.sender]) return;
        checks[x]++;
        checks[y]++;
        if (checks[x]==verificationNum){
            status[nonces[x]]=true;
            balances[x]=newbalances[x];
            
            isActivated[y]=true;
        }
    }
    function addTrade(Trade t){
        if (!isActivated[msg.sender]) return;
        if (!status[nonces[msg.sender]]) return;
        if (t.emmiter!= msg.sender)return;
        nonce++;
        txpool[nonce]=t;
        nonces[msg.sender]++;
        
    }

 
}