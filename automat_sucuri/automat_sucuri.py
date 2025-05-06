from abc import ABC, abstractmethod

class Observer(ABC):
    @abstractmethod
    def update(self, data):
        pass
class Observable:
    def __init__(self):
        self.observers = []

    def attach(self, observer):
        self.observers.append(observer)

    def detach(self, observer):
        self.observers.remove(observer)

    def notifyAll(self, data):
        for observer in self.observers:
            observer.update(data)

class State:
    def __init__(self, state_machine):
        self.state_machine = state_machine

    def client_arrived(self):
        pass

    def insert_10bani(self):
        pass

    def insert_50bani(self):
        pass

    def insert_1leu(self):
        pass

    def insert_5lei(self):
        pass

    def insert_10lei(self):
        pass

    def choose(self, product):
        pass

class TakeMoneySTM(Observable):
    def __init__(self):
        super().__init__()
        self.wait_state = WaitingForClient(self)
        self.insert_money_state = InsertMoney(self)
        self.current_state = self.wait_state
        self.money = 0

    def set_state(self, state):
        self.current_state = state

    def add_money(self, value):
        self.money += value
        self.notifyAll(self.money)

    def client_arrived(self):
        self.current_state.client_arrived()

    def insert_10bani(self):
        self.current_state.insert_10bani()

    def insert_50bani(self):
        self.current_state.insert_50bani()

    def insert_1leu(self):
        self.current_state.insert_1leu()

    def insert_5lei(self):
        self.current_state.insert_5lei()

    def insert_10lei(self):
        self.current_state.insert_10lei()

class WaitingForClient(State):
    def client_arrived(self):
        print("Clientul a sosit. Se trece la starea de inserare a banilor.")
        self.state_machine.set_state(self.state_machine.insert_money_state)

class InsertMoney(State):
    def insert_10bani(self):
        self.state_machine.add_money(0.1)

    def insert_50bani(self):
        self.state_machine.add_money(0.5)

    def insert_1leu(self):
        self.state_machine.add_money(1)

    def insert_5lei(self):
        self.state_machine.add_money(5)

    def insert_10lei(self):
        self.state_machine.add_money(10)

class SelectProductSTM(Observable):
    def __init__(self):
        super().__init__()
        self.select_product_state = SelectProduct(self)
        self.coca_cola_state = CocaCola(self)
        self.pepsi_state = Pepsi(self)
        self.sprite_state = Sprite(self)
        self.current_state = self.select_product_state

    def choose(self, product):
        self.current_state.choose(product)

    def choose_another_product(self):
        self.current_state = self.select_product_state

    def set_state(self, state):
        self.current_state = state

class SelectProduct(State):
    def choose(self, product):
        if product.lower() == "coca":
            print("CocaCola a fost selectată.")
            self.state_machine.set_state(self.state_machine.coca_cola_state)
            self.state_machine.notifyAll("coca")
        elif product.lower() == "pepsi":
            print("Pepsi a fost selectată.")
            self.state_machine.set_state(self.state_machine.pepsi_state)
            self.state_machine.notifyAll("pepsi")
        elif product.lower() == "sprite":
            print("Sprite a fost selectată.")
            self.state_machine.set_state(self.state_machine.sprite_state)
            self.state_machine.notifyAll("sprite")
        else:
            print("Produs necunoscut.")

class CocaCola(State):
    price = 5

class Pepsi(State):
    price = 4

class Sprite(State):
    price = 3

class DisplayObserver(Observer):
    def update(self, money):
        print(f"Suma introdusă: {money:.2f} lei")

class ChoiceObserver(Observer):
    def update(self, product=None):
        print(f"Produsul {product} a fost ales.")

class VendingMachineSTM:
    def __init__(self):
        self.take_money_stm = TakeMoneySTM()
        self.select_product_stm = SelectProductSTM()

        self.take_money_stm.attach(DisplayObserver())
        self.select_product_stm.attach(ChoiceObserver())

    def proceed_to_checkout(self):
        product_state = self.select_product_stm.current_state
        if hasattr(product_state, 'price'):
            price = product_state.price
            if self.take_money_stm.money >= price:
                print(f"Produs livrat! {price} lei au fost retrași.")
                self.take_money_stm.money -= price
                print(f"Rest: {self.take_money_stm.money:.2f} lei")
                self.select_product_stm.choose_another_product()
            else:
                print(f"Fonduri insuficiente. Ai {self.take_money_stm.money:.2f} lei, dar ai nevoie de {price} lei.")
        else:
            print("Niciun produs valid selectat.")

if __name__ == "__main__":
    vm = VendingMachineSTM()
    vm.take_money_stm.client_arrived()

    while True:
        print("\nMeniu:\n1. Introdu bani\n2. Alege produs\n3. Ieșire")
        choice = input("Opțiunea ta: ")

        if choice == "1":
            print("a. 10 bani\nb. 50 bani\nc. 1 leu\nd. 5 lei\ne. 10 lei")
            coin = input("Introduceți moneda: ").lower()
            if coin == "a":
                vm.take_money_stm.insert_10bani()
            elif coin == "b":
                vm.take_money_stm.insert_50bani()
            elif coin == "c":
                vm.take_money_stm.insert_1leu()
            elif coin == "d":
                vm.take_money_stm.insert_5lei()
            elif coin == "e":
                vm.take_money_stm.insert_10lei()
            else:
                print("Monedă invalidă.")

        elif choice == "2":
            product = input("Alege produsul (coca, pepsi, sprite): ")
            vm.select_product_stm.choose(product)
            vm.proceed_to_checkout()

        elif choice == "3":
            break
        else:
            print("Opțiune invalidă!")
