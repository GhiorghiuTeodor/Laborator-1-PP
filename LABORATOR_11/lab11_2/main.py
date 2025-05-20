import subprocess

def executa_pipeline(commands):
    prev_process = None

    for i, cmd in enumerate(commands):
        args = cmd.strip().split()

        if i == 0:
            # Prima comandă: stdin default, stdout conectat la PIPE
            prev_process = subprocess.Popen(args, stdout=subprocess.PIPE)
        else:
            # Comenzile următoare: stdin e stdout-ul precedent
            prev_process = subprocess.Popen(args, stdin=prev_process.stdout, stdout=subprocess.PIPE)

    # Obține output-ul final
    output, _ = prev_process.communicate()
    print(output.decode(errors="ignore"))

if __name__ == "__main__":
    cmd_input = input("Introdu o comandă cu pipe-uri (ex: echo abcd | tee b): ")
    commands = [cmd.strip() for cmd in cmd_input.split("|")]
    executa_pipeline(commands)

