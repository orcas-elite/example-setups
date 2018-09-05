from locust import HttpLocust, TaskSet, task
import datetime

class UserBehavior(TaskSet):
    @task(1)
    def a1(self):
        with self.client.get("/a1", catch_response=True) as response:
            ResponseWriter.write_response(response)

    @task(1)
    def a2(self):
        with self.client.get("/a2", catch_response=True) as response:
            ResponseWriter.write_response(response)

class ResponseWriter():
    logfile = open("response.log","a")

    @staticmethod
    def write_response(response):
        ResponseWriter.logfile.write('%s, %s, %s, %s, %.9f\n' % (datetime.datetime.now().isoformat(), response.status_code, response.reason, response.request.url, response.elapsed.total_seconds()))
        ResponseWriter.logfile.flush() # Might not be efficient under heavy load
        pass

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    min_wait = 5000
    max_wait = 9000
