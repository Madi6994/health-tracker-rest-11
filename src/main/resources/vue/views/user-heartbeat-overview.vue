<template id = "user-heartbeat-overview">

  <div>
    <h3>Exercise Goals list </h3>
    <ul>
      <li v-for="heart in heartactivity">
        {{heart.id}}: {{exercise.rate}} for {{heart.userId}} minutes
      </li>
    </ul>
  </div>
</template>

<script>
Vue.component("user-heartbeat-overview",{
  template: "#user-heartbeat-overview",
  data: () => ({
    heartactivity: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/heartbeats`)
        .then(res => this.heartactivity = res.data)
        .catch(() => alert("Error while fetching exercises"));
  }
});
</script>