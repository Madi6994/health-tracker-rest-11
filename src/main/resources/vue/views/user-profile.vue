<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noUserFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> User Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateUser()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteUser()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        <p  v-if="activities.length == 0"> No activities yet...</p>
        <p  v-if="activities.length > 0"> Activities so far...</p>
        <ul>
          <li v-for="activity in activities">
            {{ activity.description }} for {{ activity.duration }} minutes
          </li>
        </ul>
      </div>
      <div class="card-footer text-left">
        <p  v-if="exerciseactivity.length == 0"> No exercises yet...</p>
        <p  v-if="exerciseactivity.length > 0"> Exercises so far...</p>
        <ul>
          <li v-for="exercise in exerciseactivity">
            {{exercise.calories_to_burn}} for {{exercise.date}} minutes
          </li>
        </ul>
      </div>
      <div class="card-footer text-left">
        <p  v-if="healthactivity.length == 0"> No exercises yet...</p>
        <p  v-if="healthactivity.length > 0"> Exercises so far...</p>
        <ul>
          <li v-for="health in healthactivity">
            {{health.macro_percentage}} for {{health.protein_intake}} minutes
          </li>
        </ul>
      </div>
      <div class="card-footer text-left">
        <p  v-if="heartactivity.length == 0"> No heartrate yet...</p>
        <p  v-if="heartactivity.length > 0"> HeartRate so far...</p>
        <ul>
          <li v-for="heart in heartactivity">
            {{heart.rate}} for {{heart.userId}} minutes
          </li>
        </ul>
      </div>
      <div class="card-footer text-left">
        <p  v-if="coachingactivity.length == 0"> No steps yet...</p>
        <p  v-if="coachingactivity.length > 0"> Stepcounter so far...</p>
        <ul>
          <li v-for="coaching in coachingactivity">
            {{coaching.steps}} for {{coaching.userId}} minutes
          </li>
        </ul>
      </div>
    </div>
  </app-layout>
</template>

<script>

Vue.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        })
    axios.get(url + `/activities`)
        .then(res => this.activities = res.data)
        .catch(error => {
          console.log("No activities added yet (this is ok): " + error)
        })
    axios.get(url + `/exercisegoals`)
        .then(res => this.exerciseactivity = res.data)
        .catch(error => {
          console.log("No exercises added yet (this is ok): " + error)
        })
    axios.get(url + `/healthcoaching`)
        .then(res => this.healthactivity = res.data)
        .catch(error => {
          console.log("No healthcoaching added yet (this is ok): " + error)
        })
    axios.get(url + `/heartbeats`)
        .then(res => this.heartactivity = res.data)
        .catch(error => {
          console.log("No heartbeats added yet (this is ok): " + error)
        })
    axios.get(url + `/stepcounter`)
        .then(res => this.coachingactivity = res.data)
        .catch(error => {
          console.log("No stepcounter added yet (this is ok): " + error)
        })
  },
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    exerciseactivity: [],
    healthactivity: [],
    heartactivity:[],
    coachingactivity:[]
  }),
  methods: {
    updateUser: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}`
      axios.patch(url,
          {
            name: this.user.name,
            email: this.user.email
          })
          .then(response =>
              this.user.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User updated!")
    },
    deleteUser: function () {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}`
        axios.delete(url)
            .then(response => {
              alert("User deleted")
              //display the /users endpoint
              window.location.href = '/users';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>