
# The Front End reads in a file of tickets available for purchase and a file containing
# information regarding current user accounts in the system , it processes a stream of ticket purchase
# and saletransactions one at a time, and it writes out a file of ticket purchase and
# saletransactions at the end of the session.

import sys


def main():
    print('************************************* ')
    print('  Welcome to Event Ticketing System ')
    print('************************************* ')
    print('')
    current_user = Login.login()
    print('************************************* ')
    action = current_user_prompt.capture_input("Enter a command (logout, addcredit, create, delete, buy, sell, refund): ")
    while action != 'EXIT':
        action = action.lower()
        action = action.strip()
        if action == 'logout':
            current_user.user_logout(current_user)
            current_user = Login.login()
        elif action == 'buy':
            if current_user.role != 'SS':
                Ticket.buy_ticket(current_user)
            else:
                print('  Access denied.  ')
        elif action == 'sell':
            if current_user.role != 'BS':
                Ticket.sell_ticket(current_user)
            else:
                print('  Access denied.  ')
                break
        elif action == 'create':
            if current_user.role == 'AA':
                User.create_user(current_user)
            else:
                print('  Access denied.  ')
        elif action == 'delete':
            if current_user.role == 'AA':
                User.delete_user(current_user)
            else:
                print('  Access denied.  ')
        elif action == 'refund':
            if current_user.role == 'AA':
                User.refund_credit(current_user)
            else:
                print('  Access denied.  ')
        elif action == 'addcredit':
            second_action = current_user_prompt.capture_input('  Enter to add or request: ')
            if second_action == 'add' and current_user.role == 'AA':
                addcredit.add_amount(current_user)
            elif second_action == 'request':
                addcredit.request_credit_amount(current_user)
            else:
                print("  Access denied.  ")
        else:
            print('  Invalid option Selected, please try again.  ')

        action = current_user_prompt.capture_input('  Enter a command (logout, addcredit, create, delete, buy, sell, refund): ')


class Login:
    @staticmethod
    def login():
        input_prompt = current_user_prompt.capture_input('To log in, please type the word login or type Exit: ')
        while input_prompt.lower() != 'login':
            input_prompt = current_user_prompt.capture_input('To log in, please type the word \'login\' or type \'Exit\': ')
        user = current_user_prompt.capture_input('  Enter  Username: ')
        current_user = Login.get_user_accounts(user)
        while current_user == 0:
            print('  Invalid username, please try again  ')
            user = current_user_prompt.capture_input('  Enter Username: ')
            current_user = Login.get_user_accounts(user)
        print('  ***** Login successful *****  ')
        print('')
        return current_user


    @staticmethod
    def get_user_accounts(user):
        with open(uaf) as reader:
            account_file = reader.readlines()
        # reads uaf line by line and gets usernames
        for x in account_file:
            if user == x[:15].strip():
                role = x[16:18]
                credit = x[19:]
                new_user = User(user, role, float(credit))
                reader.close()
                return new_user
        print('  User not found  ')
        print('')
        reader.close()
        return 0


class User():
    def __init__(self, username, role, credit):
        self.username = username
        self.role = role
        self.credit = credit


    @staticmethod
    def user_logout(self):
        c = float(self.credit)
        with open(dtf,'w+') as writer:
            writer.write('\n00 ' + '{:<15}'.format(self.username) + ' ' + self.role + ' ' + '{:0=9.2f}'.format(c) + '\n')
        writer.close()
        print('**  Logout successful  **')

    def is_float(string):
        try:
            if float(string) or int(string):
                return True
        except ValueError:
            return False

    def create_user(self):
        name = current_user_prompt.capture_input('  Enter New user\'s Username: ')
        chars = set("@#$%^&*()+-=_~[]{}:;.,\"\'/| ")
        if (len(name) > 15) or any((c in chars) for c in name) or (len(name) < 1):
            print('  Invalid input.  ')
        else:
            while True:
                aType = current_user_prompt.capture_input('  Enter a user account type: ')
                if aType.lower() == 'admin':
                    aType = 'AA'
                    print('  Account created with type \'Admin\'  ')
                    break
                elif aType.lower() == 'full-standard':
                    aType = 'FS'
                    print('  Account created with type \'Full-Standard\'  ')
                    break
                elif aType.lower() == 'buy-standard':
                    aType = 'BS'
                    print('  Account created with type \'Buy-Standard\'')
                    break
                elif aType.lower() == 'sell-standard':
                    aType = "SS"
                    print('  Account created with type \'Sell-Standard\'  ')
                    break
                else:
                    print('  Invalid input.  \n')
            credit = current_user_prompt.capture_input("  Provide the Starting Credit(s): ")
            if not User.is_float(credit):
                print("   Invalid credit amount  ")
                credit = current_user_prompt.capture_input('  Provide the Starting Credit(s): ')
            while ((float(credit) < 0.00) or (float(credit) > 999999.00) or (len(credit.rsplit('.')[-1]) > 2)):
                print('   Invalid credit amount  ')
                credit = current_user_prompt.capture_input('  Provide the Starting Credit(s): ')
            f = open(uaf, "r+")
            exist = False
            for user in f:
                # print(user[0:15].strip())
                if user[0:15].strip() == name:
                    exist = True
                    print('   User already exists. Try again.  ')
            if not exist:
                hold = float(credit)
                f.write('\n{:<15}'.format(name) + ' ' + aType + ' ' + '{:0=9.2f}'.format(hold))
                f.close()
                f2 = open(dtf, 'w')
                f2.write('01 ' + '{:<15}'.format(name) + ' ' + aType + ' ' + '{:0=9.2f}'.format(hold))
                f2.close()
                print('***   Account created   ***')
            f.close()


    def delete_user(self):
        name = current_user_prompt.capture_input("  Enter Username to Delete from the System: ")
        account = Login.get_user_accounts(name)
        if account != 0:
            if account.username == self.username:
                print("  Can not delete user, currently user logged in. logout first!")
            else:
                with open(uaf, "r") as f:
                    lines = f.readlines()
                with open(uaf, "w") as f:
                    for line in lines:
                        if line[:15].strip() != name:
                            f.write(line)
                    f.close()
                f = open(dtf, "w")
                f.write(
                    "02 " + "{:<15}".format(self.username) + " " + self.role + " " + "{:0=9.2f}".format(self.credit))
                f.close()
                with open(atf, "w") as f:
                    for line in lines:
                        if line[23:36].strip() != name:
                            f.write(line)
                print("***  Deletion successful  ***")


    @staticmethod
    def refund_credit(user):
        buyer = current_user_prompt.capture_input("  Enter the buyer's username: ")
        seller = current_user_prompt.capture_input("  Enter the seller's username: ")
        userB = Login.get_user_accounts(buyer)
        userS = Login.get_user_accounts(seller)
        if not (userB == 0 or userS == 0):
            refundAmount = current_user_prompt.capture_input("  Credits amount for refund: ")
            floatAmount = float(refundAmount)
            while floatAmount > 999999.00:
                print("  Credit amount invalid  ")
                refundAmount = current_user_prompt.capture_input("  Credits amount for refund: ")
                floatAmount = float(refundAmount)
            if (userS.credit - floatAmount) < 0.00:
                print("  Seller does not have sufficent, can not process transaction")
            elif (userB.credit + floatAmount) > 999999.00:
                print("  Maxiumum amount of credits reached, can not process transaction")

            else:
                userS.credit = userS.credit - float(refundAmount)
                userB.credit = userB.credit + float(refundAmount)
                ra = float(refundAmount)
                file1 = open(dtf, "w+")
                file1.write("05 " + "{:<15}".format(buyer) + "{:<15}".format(seller) + "{:0=9.2f}".format(ra) + "\n")
                file1.close()

                with open(uaf, 'r') as f:
                    lines = f.readlines()
                with open(uaf, 'w') as f:
                    for line in lines:
                        if not ((line[:15].strip() == userB.username) or (line[:15].strip() == userS.username)):
                            f.write(line)
                    buyerCredit = float(userB.credit)
                    sellerCredit = float(userS.credit)
                    f.write('\n' + '{:<15}'.format(userB.username) + " " + userB.role + ' ' + '{:0=9.2f}'.format(
                        buyerCredit) + "\n")
                    f.write('{:<15}'.format(userS.username) + ' ' + userS.role + ' ' + '{:0=9.2f}'.format(sellerCredit))
                    f.close()
                    print('***  Refund completed  ***')
                    print('')


class Ticket:
    def __init__(self, event_title, sale_price, num_tickets, sellers_name, buy_sell):
        self.event_title = event_title
        self.sale_price = sale_price
        self.num_tickets = num_tickets
        self.sellers_name = sellers_name
        self.buy_sell = buy_sell

    def __str__(self):
        if self.buy_sell == '3':
            return '03 {:<19} {:<13} {:0<3} {:0=6.2f}'.format(self.event_title, self.sellers_name, self.num_tickets,
                                                              self.sale_price)
        elif self.buy_sell == '4':
            return "04 {:<20} {:<13} {:0<3} {:0=6.2f}".format(self.event_title, self.sellers_name, self.sale_price,
                                                              self.num_tickets)
        else:
            return "{:<20} {:<13} {:0<3} {:0=6.2f}".format(self.event_title, self.sellers_name, self.num_tickets,
                                                           self.sale_price)


    @staticmethod
    def sell_ticket(user):
        # open both file to get updated dtf and atf.txt
        transaction_file = open(dtf, 'a')
        account_file = open(atf, 'a')
        # Retrieve input from user for event title, sale price and number of tickets available
        e_title = current_user_prompt.capture_input('  Name of the Event(sell) : ')
        while len(e_title) > 25:
            print('  Please enter a valid Event Name !')
            e_title = current_user_prompt.capture_input('  Name of the Event(sell) : ')
        sale_price = current_user_prompt.capture_input('  Price of each/per Ticket : ')
        sale_price = float(sale_price)
        while sale_price > 999.99 or sale_price < 0:
            print('  Invalid inputs.  ')
            sale_price = current_user_prompt.capture_input('  Price of each/per Ticket : ')
            sale_price = float(sale_price)
        num_Tickets = current_user_prompt.capture_input('  Total Number of Ticket(s) to Sell : ')
        while int(num_tickets) > 100 or int(num_tickets) < 0:
            print('  Invalid inputs.')
            num_tickets = current_user_prompt.capture_input('  Total Number of Ticket(s) to Sell : ')
        print('  Event added to sell but **Ticket will be available to sell in the system after current user logout')
        # Check to see if all input values are valid
        if int(num_tickets) < 101 and float(sale_price) < 1000 and len(e_title) < 26:
            ticketnum = int(num_tickets)
            priceOfSale = int(sale_price)
            # Create ticket object for sale records in dtf
            T1 = Ticket(eTitle, priceOfSale, ticketnum, user.username, 3)
            # Create ticket object for atf.txt
            T2 = Ticket(eTitle, priceOfSale, ticketnum, user.username, 0)
            hold = int(num_tickets)
            transaction_fil.write('\n03 {:<20} {:<13} {:03d} {:0=6.2f}'.format(T1.event_title, T1.sellers_name, hold, priceOfSale))
            with open(atf, "r") as f:
                lines = f.readlines()
            with open(atf, "w") as f:
                for line in lines:
                    if line.strip('\n') != 'END':
                        f.write(line)
            account_file.write('{:<19} {:<14} {:03d} {:0=6.2f}'.format(T1.event_title, T1.sellers_name, ticketnum, priceOfSale) + '\n')
            account_file.write('END\n')
        else:
            print('  *  Invalid inputs.  *  ')
        transaction_file.close()
        account_file.close()


    @staticmethod
    def buy_ticket(user):
        transaction_file = open(dtf, 'r+')
        account_file = open(atf, 'r+')
        # Retrieve input from user for ticket they want to buy
        eTitle = current_user_prompt.capture_input('  Enter event Name/Tile (buy): ')
        sellers_name = current_user_prompt.capture_input('  Enter the seller\'s username: ')
        exist = False
        confirmation = ''
        # Check to see if the ticket they want exists
        for ticket in account_file:
            matchT = ticket[:20].strip()
            matchS = ticket[20:33].strip()
            # If the event title and seller name are correct move forward in th ebuying process
            if matchT.lower() == eTitle.lower() and matchS.lower() == sellers_name.lower():
                exist = True

                # Create ticket object for easier access to values
                T = Ticket(matchT, ticket[39:].strip(), ticket[33:39].strip(), matchS, 4)
                # Get number of tickets wanted from user
                num_tickets = current_user_prompt.capture_input('  Enter number of ticket(s) to buy: ')
                # Verify amount is valid for user requesting

                if int(num_tickets) < 1:
                    print('  Invalid number of tickets')
                    break
                if not user.role == 'AA':
                    if int(num_tickets) > 4:
                        print('  Invalid number of tickets')
                        break
                # Check to see if there are enough tickets for sale
                if int(num_tickets) > int(T.num_tickets):
                    print('  Not enough tickets available for sale.')
                    break
                # Calculate final cost
                total = int(num_tickets) * float(T.sale_price)
                hold = float(T.sale_price)
                confirmation = current_user_prompt.capture_input('  Each/per ticket price: $' + '{:.2f}'.format(hold) + ' | Total Ticket(s) price: $'
                                          + '{:.2f}'.format(total) + '. To Confirm Perchase type yes/no: ')
                # Don't proceed if user does not confirm
                if confirmation.lower() == 'no':
                    print('  Transaction cancelled\n')
                    break
                if confirmation.lower() == 'yes':
                    print('  ***  Ticket purchased.  ***\n')
                transaction_file.write('\n')
                ticketnum = int(T.num_tickets) - int(num_tickets)
                transaction_file.write('04 {:<20} {:<13} {:03d} {:0=6.2f}'.format(T.event_title, T.sellers_name, ticketnum, hold))
                transaction_file.close()
                account_file.close()
                # Update atf.txt file with the correct amount of tickets available
                with open(atf, 'r') as f:
                    lines = f.readlines()
                with open(atf, 'w') as f:
                    for line in lines:
                        if (line[:20].strip().lower() != eTitle.lower() and line[
                                                                            20:33].strip().lower() != sellers_name.lower()) and (
                                line.strip('\n') != 'END'):
                            f.write(line)
                    T.num_tickets = ticketnum
                    f.write('{:<19} {:<14} {:03d} {:0=6.2f}'.format(T.event_title, T.sellers_name, ticketnum, hold))
                    f.write('\nEND')
                break
        if not exist:
            print('*  Ticket not found  *\n')


class addcredit:
    @staticmethod
    def add_amount(user):
        c = current_user_prompt.capture_input('  Enter the credit amount: ')
        if float(c) > 1000.00:
            print('  ** Invalid amount ** ')
        else:
            uName = current_user_prompt.capture_input('  Enter the username to which credit is being added: ')
            total = float(c)
            with open(dtf, "r") as f:
                lines = f.readlines()
            with open(dtf, 'w') as f:
                for x in lines:
                    if x[:3].strip() == '06':
                        if uName == x[3:18].strip():
                            value = float(x[23:].strip('\n'))
                            total += value
                    if x[:3].strip() == '00':
                        total = float(c)
                    f.write(x)
            if total > 1000.00:
                print('  * Credit can not be added *\n   *** As maximum limit amount exceeded **** \n')
            else:
                account = Login.get_user_accounts(uName)
                test = Login.get_user_accounts(uName)
                # Update amount of credit user has available
                account.credit = account.credit + float(c)
                # Record transaction in dtf
                hold = float(c)
                f = open(dtf, 'a')
                f.write(
                    '\n06 ' + '{:<15}'.format(account.username) + ' ' + account.role + ' ' + '{:0=9.2f}'.format(hold))
                f.close()
                # Update uaf.txt file with the users new available credit amount
                with open(uaf, 'r') as f:
                    lines = f.readlines()
                with open(uaf, 'w') as f:
                    for line in lines:
                        if line[:15].strip() != account.username:
                            f.write(line)
                    f.write('\n')
                    f.write('{:<15}'.format(account.username) + ' ' + account.role + ' ' + '{:0=9.2f}'.format(
                        account.credit))
                    f.close()
                print('  *** Credit Added successfully ***  \n')


    @staticmethod
    def request_credit_amount(user):
        f = open(dtf, 'w')
        credit = current_user_prompt.capture_input('  Enter a credit request amount: ')
        hold = float(credit)
        f.write('\n07 ' + '{:<15}'.format(user.username) + ' ' + user.role + ' ' + '{:0=9.2f}'.format(hold))
        f.close()
        print('  *** Credit Request Made !  \n')


class SavePrompt:
    @staticmethod
    def capture_input(input_prompt):
        current_input = input(input_prompt)
        if current_input == ('EXIT'):
            sys.exit()
        return current_input.strip()


if __name__ == '__main__':
    uaf = 'user_account_file.txt'
    atf = 'available_tickets_file.txt'
    dtf = 'daily_transaction_file.txt'
    # save current user of the system
    current_user_prompt = SavePrompt()
    # main system logon with this function.
    main()
