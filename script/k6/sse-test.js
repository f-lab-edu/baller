import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        sse_clients: {
            executor: 'constant-vus',
            exec: 'sseConnection',
            vus: 1500,
            duration: '120s',
        },
        event_producers: {
            executor: 'constant-arrival-rate',
            exec: 'sendEvent',
            rate: 1,
            timeUnit: '1s',
            duration: '60s',
            preAllocatedVUs: 100,
            maxVUs: 100,
        },
    },
};

const BASE_URL = 'http://localhost:8080';
const GAME_ID = 3;
const SSE_ENDPOINT = `${BASE_URL}/api/games/${GAME_ID}/records`;
const EVENT_ENDPOINT = `${BASE_URL}/api/games/${GAME_ID}/members/30/basketball/record`;
const LOGIN_ENDPOINT = `${BASE_URL}/api/members/login`;

const USER_CREDENTIALS = JSON.stringify({
    email: 'club@flab.com',
    password: 'Flab@1234',
});

// ✅ setup()에서 로그인 1회 실행 → 모든 VU가 token 공유
export function setup() {
    const res = http.post(LOGIN_ENDPOINT, USER_CREDENTIALS, {
        headers: { 'Content-Type': 'application/json' },
    });

    check(res, {
        '로그인 성공': (r) => r.status === 200 && r.json('accessToken') !== undefined,
    });

    const token = res.json('accessToken');
    return { token }; // → sendEvent, sseConnection 에 전달됨
}

// 1) SSE 연결 테스트
export function sseConnection(data) {
    const res = http.get(SSE_ENDPOINT, {
        headers: { Accept: 'text/event-stream' },
        timeout: '70s',
    });

    check(res, {
        'SSE 연결 성공': (r) => {
            if (!r || r.status !== 200) {
                console.error(`[FAIL] status=${r?.status}, body=${r?.body?.slice(0, 100)}`);
            }
            return r.status === 200;
        },
    });

    sleep(60); // 연결 유지
}

// 2) 이벤트 전송 테스트
export function sendEvent(data) {
    const token = data.token;

    const body = JSON.stringify({ points: 2 }); // 숫자형으로 변경

    const res = http.patch(EVENT_ENDPOINT, body, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
    });

    check(res, {
        '이벤트 전송 성공': (r) => r.status === 200,
    });

    sleep(1);
}
