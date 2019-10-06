<template>
    <v-content>
        <v-container fluid>
            <v-row>
                <v-col>
                    <v-card>
                        {{assetData}}
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
    </v-content>

</template>

<script>
    import Api from "../components/backend-api";

    export default {

        name: "Asset",
        props: {
            assetId: String
        },
        data() {
            return {
                assetData: {}
            }
        },
        mounted() {
            Api.getAsset(this.assetId).then(response => {
                this.assetData = response.data
            })
        }
    }
</script>

<style scoped>

</style>