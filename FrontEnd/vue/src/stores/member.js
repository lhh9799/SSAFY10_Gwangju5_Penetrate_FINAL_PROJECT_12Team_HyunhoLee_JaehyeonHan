import { ref } from "vue";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import { jwtDecode } from "jwt-decode";

import {
  userConfirm,
  findById,
  tokenRegeneration,
  logout,
  join,
  withdraw,
  check,
  modifyPassword,
} from "@/api/user";
import { httpStatusCode } from "@/util/http-status";

export const useMemberStore = defineStore("memberStore", () => {
  const router = useRouter();

  const isLogin = ref(false);
  const isLoginError = ref(false);
  const userInfo = ref(null);
  const isValidToken = ref(false);

  const userLogin = async (loginUser) => {
    await userConfirm(
      loginUser,
      (response) => {
        // console.log("login ok!!!!", response.status);
        // console.log("login ok!!!!", httpStatusCode.CREATE);
        if (response.status === httpStatusCode.CREATE) {
          let { data } = response;
          // console.log("data", data);
          let accessToken = data["access-token"];
          let refreshToken = data["refresh-token"];
          console.log("accessToken", accessToken);
          console.log("refreshToken", refreshToken);
          isLogin.value = true;
          isLoginError.value = false;
          isValidToken.value = true;
          sessionStorage.setItem("accessToken", accessToken);
          sessionStorage.setItem("refreshToken", refreshToken);
          console.log("sessiontStorage에 담았다", isLogin.value);
        } else {
          console.log("로그인 실패했다");
          isLogin.value = false;
          isLoginError.value = true;
          isValidToken.value = false;
        }
      },
      (error) => {
        console.error(error);
      }
    );
  };

  const getUserInfo = (token) => {
    let decodeToken = jwtDecode(token);
    console.log("2. decodeToken", decodeToken);
    findById(
      decodeToken.userId,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          userInfo.value = response.data.userInfo;
          console.log("3. getUserInfo data >> ", response.data);
        } else {
          console.log("유저 정보 없음!!!!");
        }
      },
      async (error) => {
        console.error(
          "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
          error.response.status
        );
        isValidToken.value = false;

        await tokenRegenerate();
      }
    );
  };

  const tokenRegenerate = async () => {
    console.log("토큰 재발급 >> 기존 토큰 정보 : {}", sessionStorage.getItem("accessToken"));
    await tokenRegeneration(
      JSON.stringify(userInfo.value),
      (response) => {
        if (response.status === httpStatusCode.CREATE) {
          let accessToken = response.data["access-token"];
          console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
          sessionStorage.setItem("accessToken", accessToken);
          isValidToken.value = true;
        }
      },
      async (error) => {
        // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
        if (error.response.status === httpStatusCode.UNAUTHORIZED) {
          console.log("갱신 실패");
          // 다시 로그인 전 DB에 저장된 RefreshToken 제거.
          await logout(
            userInfo.value.userid,
            (response) => {
              if (response.status === httpStatusCode.OK) {
                console.log("리프레시 토큰 제거 성공");
              } else {
                console.log("리프레시 토큰 제거 실패");
              }
              alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
              isLogin.value = false;
              userInfo.value = null;
              isValidToken.value = false;
              router.push({ name: "user-login" });
            },
            (error) => {
              console.error(error);
              isLogin.value = false;
              userInfo.value = null;
            }
          );
        }
      }
    );
  };

  const userLogout = async (userid) => {
    await logout(
      userid,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          isLogin.value = false;
          userInfo.value = null;
          isValidToken.value = false;
        } else {
          console.error("유저 정보 없음!!!!");
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };

  const userJoin = async (joinUser) => {
    await join(
      joinUser,
      (response) => {
        if (response.status === httpStatusCode.OK || response.status === httpStatusCode.CREATE) {
          console.log("회원가입 정상 처리");
          alert("회원가입 되었습니다!");
        } else {
          console.log("response.status");
          console.log(response.status);
          console.error("회원가입 중 오류 발생 (서버 측)");
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };

  const userWithdraw = async (userid) => {
    await withdraw(
      userid,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          console.log("유저 탈퇴 완료!!!");
        } else {
          console.error("유저 정보 없음!!!!");
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };

  const checkPwd = async (userInput) => {
    await check(
      userInput,
      (response) => {
        if (response.status === httpStatusCode.CREATE) {
          console.log("비밀번호가 일치함");
          alert("비밀번호 일치");
          router.push({ name: "user-mypage" });
        } else {
          console.log("response.status");
          console.log(response.status);
          alert("비밀번호가 다릅니다.");
          router.replace({ name: "check" });
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };

  const userModifyPwd = async (modifyPwd) => {
    await modifyPassword(
      modifyPwd,
      (response) => {
        if (response.status === httpStatusCode.OK) {
          console.log("비밀번호 변경 완료!!!");
          alert("비밀번호 변경 완료!!!");
        } else {
          console.error("비밀번호 변경 중 문제 발생!!!");
          alert("비밀번호 변경 중 문제 발생!!!");
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };

  return {
    isLogin,
    isLoginError,
    userInfo,
    isValidToken,
    userLogin,
    getUserInfo,
    tokenRegenerate,
    userLogout,
    userJoin,
    userWithdraw,
    checkPwd,
    userModifyPwd,
  };
  // {
  //   persist: {
  //     storage: sessionStorage,
  //   },
  // }
});
