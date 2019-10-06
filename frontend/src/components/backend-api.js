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

    getLineChart(period, assetId) {
        return AXIOS.get('/chart/line', {
            params: {'period': period, 'assetId': assetId}
        });
    },

    getPieChart(assetClasses) {
        return AXIOS.get('/chart/pie', {
            params: {'assetClasses': assetClasses}
        });
    },

    getAssets() {
        return AXIOS.get('/assets');
    },

    getAsset(assetId) {
        return AXIOS.get('/assets/' + assetId);
    }
}
