import axios from 'axios'

const AXIOS = axios.create({
    baseURL: '/api',
    timeout: 1000
});


export default {
    getChart(period, tags) {
        return AXIOS.get('/chart/stacked', {
            params: {'period': period, 'assetClasses': tags}
        });
    },

    getPieChart(assetClasses) {
        return AXIOS.get('/chart/pie', {
            params: {'assetClasses': assetClasses}
        });
    },

    getAssets() {
        return AXIOS.get('/assets');
    }
}
