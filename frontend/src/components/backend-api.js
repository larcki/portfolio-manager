import axios from 'axios'

const AXIOS = axios.create({
    baseURL: '/api',
    timeout: 10000
});


export default {

    getGeneric(period, chartType) {
        return AXIOS.get('/chart/line', {
            params: {'period': period, 'chartType': chartType}
        });
    },

    getChart(period, tags) {
        return AXIOS.get('/chart/stacked', {
            params: {'period': period, 'assetClasses': tags}
        });
    },

    getLineChart(period, assetId) {
        return AXIOS.get('/chart/performance', {
            params: {'period': period, 'assetId': assetId}
        });
    },

    getPieChart() {
        return AXIOS.get('/chart/pie');
    },

    getAssets() {
        return AXIOS.get('/assets');
    },

    getAssetsForSelection() {
        return AXIOS.get('/assets-min');
    },

    getAsset(assetId) {
        return AXIOS.get('/assets/' + assetId);
    },

    getTransactions() {
        return AXIOS.get('/transaction');
    },

    saveTransaction(transaction) {
        return AXIOS.post('/transaction', transaction);
    },

    getSummary() {
        return AXIOS.get('/summary')
    },

    getSummarySince(days) {
        return AXIOS.get('/summary/', {
            params: {'days': days}
        })
    }


}
