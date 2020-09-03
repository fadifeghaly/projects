import smtplib
from email.mime.text import MIMEText
import ssl
from twilio.rest import Client
import datetime
import time
from datetime import timedelta

port = 587
smtp_server = "smtp.gmail.com"
sender_email = "projet.session.6150@gmail.com"
password = "Not available"
context = ssl.create_default_context()

# Commentaires clients
def send_comment(nom, prenom, courriel, message):
    with smtplib.SMTP(smtp_server, port) as server:
        server.ehlo()
        server.starttls(context=context)
        server.ehlo()
        server.login(sender_email, password)
        commentaire = "- Nom - " + prenom + " " + nom + "\n" + \
            "- Adresse Courriel - " + courriel + "\n" + "- Commentaire - " + message
        server.sendmail(sender_email, sender_email, commentaire)

# Confirmation du rdv par courriel
def send_confirmation_email(
        receiver_email,
        subj,
        msg):
    with smtplib.SMTP(smtp_server, port) as server:
        server.ehlo()
        server.starttls(context=context)
        server.ehlo()
        server.login(sender_email, password)
        body = MIMEText(msg)
        body['Subject'] = subj
        body['From'] = sender_email
        body['To'] = receiver_email
        server.sendmail(sender_email, receiver_email, body.as_string())

# Confirmation du rdv par sms
# C'est mon token personnel, plz don't share it
def send_sms(num_telephone, msg):
    client = Client(
        'Not available',
        'Not available')
    message = client.messages .create(
        body=msg,
        from_='Not available',
        to=num_telephone)
        