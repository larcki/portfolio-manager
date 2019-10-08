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
                                :search="search">
                            <template v-slot:item.name="{item}">
                                <router-link :to="{path: '/assets/' + item.id }">{{item.name}}</router-link>
                            </template>
                        </v-data-table>
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
                    {text: 'Account', value: 'account'},
                    {text: 'Value', value: 'totalValue'},
                    {text: 'Performance', value: 'performance'},
                    {text: 'Performance (%)', value: 'performancePercentage'},
                ],
                assets: [],
            }
        },
        mounted() {
            Api.getAssets()
                .then(response => {
                    this.assets = response.data
                })
        }
    }

</script>
<style scoped>

</style>