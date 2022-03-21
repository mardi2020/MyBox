import client from "./client";
import async from "async";

// 로그인
export const login = async (email, password) => {
    const response = await client.post("http://192.168.0.26:8080/user/login", {
        email: email,
        password: password
    })

    return response;
}

// 회원가입
export const register = (email, password, userName) => {
    client.post("http://192.168.0.26:8080/user/register", {
        email: email,
        password: password,
        userName: userName
    }).then(function (response){
    }).catch(function (error) {
    }).then(function () {});
}

//비밀번호 찾기
export const findPassword = async (email) => {
    const response = await client.post("http://192.168.0.26:8080/user/password/find", {
        email
    })
}

// 비밀번호 변경
export const resetPassword = async (email, passwordBefore, passwordAfter) => {
    const response = await client.post("http://192.168.0.26:8080/user/password/reset", {
        email,
        passwordBefore,
        passwordAfter
    })
    return response
}