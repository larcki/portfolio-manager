<template>
    <v-content>
        <v-container fluid>
            <v-row>
                <v-col>
                    <v-card>
                        <v-card-title>
                            Assets
                            <div class="flex-grow-1"></div>
                            <v-text-field
                                    v-model="search"
                                    label="Search"
                                    single-line
                                    hide-details
                            ></v-text-field>
                        </v-card-title>
                        <v-data-table
                                hide-default-footer="true"
                                :headers="headers"
                                :items="assets"
                                :search="search"
                        ></v-data-table>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
    </v-content>
</template>

<script>
    import Api from "../components/backend-api";


    export default {
        data() {
            return {
                search: '',
                headers: [
                    {
                        text: 'Asset',
                        align: 'left',
                        sortable: false,
                        value: 'name',
                    },
                    {text: 'Value', value: 'value'},
                    {text: 'Profit', value: 'profit'},
                    {text: 'Profit (%)', value: 'profitPercent'},
                ],
                assets: [],
            }
        },
        mounted() {
            Api.getAssets()
                .then(response => {
                    console.log(response.data)
                    this.assets = response.data
                })
        },

    }

</script>
<style scoped>

</style>