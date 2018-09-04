from locust import HttpLocust, TaskSet, task

class UserBehavior(TaskSet):
    @task(1)
    def a1(self):
        with self.client.get("/a1") as response:
            self.log_response(response)

    @task(1)
    def a2(self):
        with self.client.get("/a2") as response:
            self.log_response(response)

    def log_response(self, response):
        print('%s, %s, %s, %.12f' % (response.status_code, response.reason, response.request.url, response.elapsed.total_seconds()))
        # TODO: write to file

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    min_wait = 5000
    max_wait = 9000
