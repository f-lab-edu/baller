import http from 'k6/http';
import { check } from 'k6';
import { randomIntBetween, randomString } from "https://jslib.k6.io/k6-utils/1.1.0/index.js";

export let options = {
    vus: 50, // 가상 사용자 수
    duration: '30s', // 테스트 지속 시간
};

export default function () {
    // 랜덤 데이터 생성
    const email = `test+${randomString(6)}@flab.com`;
    const password = 'Flab@1234';
    const name = '테스트';
    const phoneNumber = `010-${Math.floor(Math.random() * 9000 + 1000)}-${Math.floor(Math.random() * 9000 + 1000)}`;

    // JSON 바디 구성
    const body = JSON.stringify({
        email: email,
        password: password,
        name: name,
        phoneNumber: phoneNumber
    });

    // HTTP POST 요청 설정
    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // POST 요청 보내기
    let response = http.post('http://localhost:8080/api/members', body, params);

    // 응답 확인
    check(response, {
        'is status 200': (r) => r.status === 200,
    });
}
