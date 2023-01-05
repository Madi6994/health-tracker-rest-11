<template id = "user-stepcounter-overview">

  <div>
    <h3>Step Counter list </h3>
    <ul>
      <li v-for="coaching in coachingactivity">
        {{coaching.id}}: {{coaching.steps}} for {{coaching.userId}} minutes
      </li>
    </ul>
  </div>
</template>

<script>
Vue.component("user-stepcounter-overview",{
  template: "#user-stepcounter-overview",
  data: () => ({
    coachingactivity: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/stepcounter`)
        .then(res => this.coachingactivity = res.data)
        .catch(() => alert("Error while fetching exercises"));
  }
});
</script>