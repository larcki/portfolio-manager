<template>
    <v-app>

        <v-app-bar color="primary" hide-on-scroll dark app>
            <v-toolbar-items>
                <v-btn to="/" text>Dashboard</v-btn>
                <v-btn to="/assets" text>Assets</v-btn>
                <v-btn to="/transactions" text>Transactions</v-btn>
            </v-toolbar-items>
            <v-spacer></v-spacer>
            <v-btn text
                   href="https://github.com/larcki/portfolio-manager"
                   target="_blank">
                <v-icon>mdi-github-circle</v-icon>
            </v-btn>
        </v-app-bar>

        <router-view></router-view>

    </v-app>
</template>

<script>
    export default {
        name: 'app',
        components: {}
    }
</script>
