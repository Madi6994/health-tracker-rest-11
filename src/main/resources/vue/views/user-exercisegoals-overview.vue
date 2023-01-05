<template id = "user-exercisegoals-overview">

  <div>
    <h3>Exercise Goals list </h3>
    <ul>
      <li v-for="exercise in exerciseactivity">
        {{exercise.id}}: {{exercise.userId}}: {{exercise.calories_to_burn}}: {{exercise.steps}} for {{exercise.date}} minutes
      </li>
    </ul>
  </div>
</template>

<script>
Vue.component("user-exercisegoals-overview",{
  template: "#user-exercisegoals-overview",
  data: () => ({
    exerciseactivity: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/exercisegoals`)
        .then(res => this.exerciseactivity = res.data)
        .catch(() => alert("Error while fetching exercises"));
  }
});
</script>