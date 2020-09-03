import smtplib
import ssl

port = 587
smtp_server = "smtp.gmail.com"
sender_email = "projet.session.INF5190@gmail.com"
password = "not available"
context = ssl.create_default_context()
message = """\
Subject: Nouvelle notification - inspection alimentaire MTL

Voici la liste des nouveaux contrevenants : \n\n"""


def send_email(liste_contrevenants, courriel):
    with smtplib.SMTP(smtp_server, port) as server:
        server.ehlo()
        server.starttls(context=context)
        server.ehlo()
        server.login(sender_email, password)
        server.sendmail(sender_email, courriel, message + liste_contrevenants)
