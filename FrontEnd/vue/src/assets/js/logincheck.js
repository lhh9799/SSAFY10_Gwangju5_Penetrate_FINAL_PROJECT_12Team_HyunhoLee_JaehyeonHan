        /*세션을 다룰줄 몰라서 로컬 스토리지로 로그인한 정보 저장*/
        let nowLoginId = localStorage.getItem('loginSession');
        window.onload = function(){
        if(nowLoginId ===null){
          document.getElementById('logoutState').style = 'display: inline-flex;';
          document.getElementById('loginState').style = 'display: none;';
        }
        else{
          nowLoginInfo=JSON.parse(nowLoginId);
          document.getElementById('nowLonginMessage').innerText = `${nowLoginInfo.name}님 환영합니다`;
          document.getElementById('logoutState').style = 'display: none;';
          document.getElementById('loginState').style = 'display: inline-flex;';
        }
        };
        
        function handleLogout(){
            localStorage.removeItem('loginSession');
            window.location.replace('index.html');
          }