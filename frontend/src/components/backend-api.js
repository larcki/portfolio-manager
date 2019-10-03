import axios from 'axios'

const AXIOS = axios.create({
    baseURL: '/api',
    timeout: 1000
});


export default {
    getChart(period, chartType, tags, assets) {
        return AXIOS.get('/chart', {
            params: {'period': period, 'chartType': chartType, 'assetClasses': tags, 'assets': assets}
        });
    }
}
