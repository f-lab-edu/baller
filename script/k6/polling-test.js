import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        polling_clients: {
            executor: 'constant-arrival-rate',
            exec: 'pollingRequest',
            rate: 333, // 3초당 1000명 ≒ 약 333.33 req/s
            timeUnit: '1s',
            duration: '60s',
            preAllocatedVUs: 100,
            maxVUs: 1000,
        },
    },
};

const BASE_URL = 'http://localhost:8080';
const GAME_ID = 3;
const POLLING_ENDPOINT = `${BASE_URL}/api/games/${GAME_ID}/records/polling`;

// 1) polling 요청 테스트
export function pollingRequest() {
    const res = http.get(POLLING_ENDPOINT);

    check(res, {
        'Polling 응답 성공': (r) => {
            if (!r || r.status !== 200) {
                console.error(`[FAIL] status=${r?.status}, body=${r?.body?.slice(0, 100)}`);
            }
            return r.status === 200;
        },
    });

    sleep(1); // 다음 polling 요청까지 잠시 대기 (optional)
}
