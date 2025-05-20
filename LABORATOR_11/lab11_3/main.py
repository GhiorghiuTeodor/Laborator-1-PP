import threading
from queue import Queue
import time

class ThreadPool:
    def __init__(self, num_threads):
        self.num_threads = num_threads
        self.tasks = Queue()
        self.threads = []
        self.stop_event = threading.Event()

        for _ in range(num_threads):
            t = threading.Thread(target=self.worker)
            t.start()
            self.threads.append(t)

    def worker(self):
        while not self.stop_event.is_set():
            try:
                func, args = self.tasks.get(timeout=0.1)  # task este tuplu functie si argument
                func(*args)
                self.tasks.task_done()
            except Exception:
                continue

    def map(self, func, iterable):
        n = len(iterable)
        base, extra = divmod(n, self.num_threads)
        # imparte elementele pentru ca fiecare thread sa primeasca un nr egal, dar primele pot primi in plus
        index = 0
        for i in range(self.num_threads):
            # cate elemente primeste thread ul i
            count = base + (1 if i < extra else 0)
            # extrage segmentul de elemente pentru thread-ul i
            segment = iterable[index:index + count]
            index += count
            if segment:  # Daca segmentul nu este gol
                # Adauga in coada de task-uri o functie care aplica func pe segment
                self.tasks.put((self._apply_on_segment, (func, segment)))

    def _apply_on_segment(self, func, segment):
        for item in segment:
            func(item)

    def join(self):
        self.tasks.join()
        # asteapta sa se termine toate

    def terminate(self):
        self.stop_event.set()  # semnal  de oprire a executiei thread-urilor
        for t in self.threads:
            t.join()

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.join()  # asteapta sa se termine
        self.terminate()  # preste toate thread-urile


def procesare(x):
    print(f"Procesez {x} in thread {threading.current_thread().name}")
    time.sleep(0.3)  # simuleaza o munca care dureaza 0.3 secunde


if __name__ == "__main__":
    data = list(range(9))
    with ThreadPool(4) as pool:
        pool.map(procesare, data)
