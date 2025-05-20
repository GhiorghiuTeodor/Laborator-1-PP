import asyncio

async def calc_sum(name, queue):
        n = await queue.get()
        s = sum(range(n + 1))
        print(f"Corutina {name} a calculat suma de la 0 la {n}: {s}")
        queue.task_done()
async def main():
    queue = asyncio.Queue()
    for n in [10, 100, 1000, 10000]:
        await queue.put(n)
    corutine = [asyncio.create_task(calc_sum(f"-{i+1}", queue)) for i in range(4)]
    await queue.join()
    for w in corutine:
        w.cancel()

if __name__ == '__main__':
    asyncio.run(main())
