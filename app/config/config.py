import os
from dotenv import load_dotenv
from openai import OpenAI

# Load the .env into environment variables
load_dotenv()

client = OpenAI(
    organization=os.getenv("OPENAI_ORGANIZATION_ID"),
    project=os.getenv("OPENAI_PROJECT_ID"),
)
