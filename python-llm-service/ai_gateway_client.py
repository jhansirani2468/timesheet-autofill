import os
import requests
from dotenv import load_dotenv
load_dotenv()

GATEWAY_URL = os.getenv("GATEWAY_URL")       # e.g. https://gateway.company.com
TENANT_ID = os.getenv("TENANT_ID")
CLIENT_ID = os.getenv("CLIENT_ID")
CLIENT_SECRET = os.getenv("CLIENT_SECRET")

# Provided functions from company (adapted)
def generate_bearer_token():
    token_url = f"{GATEWAY_URL}/oauth2/token"
    payload = {
        "grant_type": "client_credentials",
        "client_id": CLIENT_ID,
        "client_secret": CLIENT_SECRET,
        "tenant_id": TENANT_ID
    }
    r = requests.post(token_url, data=payload, timeout=30)
    r.raise_for_status()
    j = r.json()
    return j.get("access_token")

def invoke_bedrock_model(access_token: str, prompt: str):
    url = f"{GATEWAY_URL}/v1/chat/completions"
    headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }
    body = {
        "model": os.getenv("GATEWAY_MODEL","claude-3-sonnet"),
        "messages": [{"role": "user", "content": prompt}],
        "max_tokens": 1500,
        "temperature": 0.2
    }
    r = requests.post(url, json=body, headers=headers, timeout=60)
    r.raise_for_status()
    return r.json()
