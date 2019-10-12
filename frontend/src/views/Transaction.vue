<template>
    <v-content>
        <v-container fluid>
            <v-row>
                <v-col cols="12" sm="4">
                    <v-card :elevation="n - 1">
                        <v-card-title>
                            Add transaction
                        </v-card-title>
                        <v-card-text>
                            <v-form ref="form">

                                <v-col>
                                    <v-row>

                                        <v-autocomplete
                                                label="Asset"
                                                :items="assets"
                                                item-text="name"
                                                item-value="id"
                                                v-model="selectedAsset"
                                        ></v-autocomplete>

                                    </v-row>
                                    <v-row>

                                        <v-text-field
                                                v-model="newTransaction.quantityChange"
                                                label="Quantity"
                                        ></v-text-field>
                                    </v-row>

                                    <v-row>


                                        <v-text-field
                                                v-model="newTransaction.totalPrice"
                                                label="Total amount"
                                        ></v-text-field>
                                    </v-row>

                                    <v-row>

                                        <v-text-field
                                                v-model="newTransaction.unitPrice"
                                                label="Unit price"
                                        ></v-text-field>
                                    </v-row>

                                    <v-row>

                                        <v-select
                                                :items="currencies"
                                                v-model="newTransaction.currency"
                                                label="Currency"

                                        ></v-select>

                                    </v-row>

                                </v-col>

                                <v-btn color="primary" @click="saveTransaction()">Save</v-btn>

                            </v-form>
                        </v-card-text>
                    </v-card>
                </v-col>


                <v-col cols="12" sm="8">
                    <v-card :elevation="n - 1">
                        <v-card-title>
                            Transactions
                        </v-card-title>
                        <v-card-text>

                            <v-simple-table>
                                <template v-slot:default>
                                    <thead>
                                    <tr>
                                        <th class="text-left">Time</th>
                                        <th class="text-left">Asset</th>
                                        <th class="text-left">Account</th>
                                        <th class="text-left">Total amount</th>
                                        <th class="text-left">Fee</th>
                                        <th class="text-left">Currency</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr v-for="item in transactions" :key="item.time">
                                        <td>{{ item.time }}</td>
                                        <td>{{ item.asset }}</td>
                                        <td>{{ item.account }}</td>
                                        <td>{{ item.totalAmount }}</td>
                                        <td>{{ item.fee }}</td>
                                        <td>{{ item.currency }}</td>
                                    </tr>
                                    </tbody>
                                </template>
                            </v-simple-table>

                        </v-card-text>
                    </v-card>
                </v-col>


            </v-row>
        </v-container>
        <v-snackbar
                v-model="snackbar"
                color="info"
                :timeout=5000>
            Transaction saved
            <v-btn
                    text
                    @click="snackbar = false">
                Close
            </v-btn>
        </v-snackbar>
    </v-content>
</template>

<script>
    import Api from "../components/backend-api";

    export default {

        name: "Transaction",
        data() {
            return {
                snackbar: false,
                transactions: [],
                newTransaction: {},
                assets: [],
                selectedAsset: {},
                currencies: ['EUR', 'GBP']
            }
        },
        mounted() {
            Api.getTransactions()
                .then(response => {
                    this.transactions = response.data
                })

            Api.getAssetsForSelection()
                .then(response => {
                    this.assets = response.data
                })
        },
        methods: {
            saveTransaction() {
                this.newTransaction['timestamp'] = new Date()
                this.newTransaction['assetId'] = this.selectedAsset
                Api.saveTransaction(this.newTransaction)
                    .then(() => {
                        this.resetForm()
                        this.snackbar = true
                        Api.getTransactions()
                            .then(response => {
                                this.transactions = response.data
                            })
                    })
            },
            resetForm() {
                this.$refs.form.reset()
            }
        }
    }
</script>

<style scoped>

</style>