<template id = "user-trackingwaterintake-overview">

  <div>
    <h3>Water Intake list </h3>
    <ul>
      <li v-for="water in waterintakeactivity">
        {{water.id}}: {{water.glass_of_water}}: {{water.userId}} for {{water.datetime}} minutes
      </li>
    </ul>
  </div>
</template>

<script>
Vue.component("user-trackingwaterintake-overview",{
  template: "#user-trackingwaterintake-overview",
  data: () => ({
    waterintakeactivity: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/trackingwaterintake`)
        .then(res => this.waterintakeactivity = res.data)
        .catch(() => alert("Error while fetching waterintake"));
  }
});
</script>