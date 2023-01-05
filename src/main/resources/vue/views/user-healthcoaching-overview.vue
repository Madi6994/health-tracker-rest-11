<template id = "user-healthcoaching-overview">

  <div>
    <h3>Health Coaching list </h3>
    <ul>
      <li v-for="health in healthactivity">
        {{health.id}}: {{health.userId}}: {{health.macro_percentage}} for {{health.protein_intake}} minutes
      </li>
    </ul>
  </div>
</template>

<script>
Vue.component("user-healthcoaching-overview",{
  template: "#user-healthcoaching-overview",
  data: () => ({
    healthactivity: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/healthcoaching`)
        .then(res => this.healthactivity = res.data)
        .catch(() => alert("Error while fetching exercises"));
  }
});
</script>