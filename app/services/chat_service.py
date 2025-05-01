from fastapi import UploadFile
from pydantic import BaseModel
from app.services.speech_service import transcribe_speech
from app.config.config import client
import aiofiles
from cachetools import TTLCache
import random
import json

class ChatCache(BaseModel):
    history: list
    language: str

# In-memory cache with TTL
# Arguments: maxsize (number of sessions), ttl (seconds)
conversation_cache = TTLCache(maxsize=100, ttl=3600)  # 100 sessions max, 1 hour TTL

async def create_chat(
    problem_id: str,
    language: str,
):
    # Initialize history from cache
    history = []

    # Create language selection prompt
    language_selection_prompt = {
        "role": "system",
        "content": f"In this conversation, you will be speaking in {language}.",
    }

    # Load interviewer behavior instructions
    interviewer_behavior = await load_json_file("app/prompts/behavior.json")

    interviewer_behavior_prompt = {
        "role": "system",
        "content": str(interviewer_behavior),
    }

    # Load interview problem statement
    problem = await load_json_file(f"app/prompts/problems/{problem_id}.json")
    problem["phase"] = "problem_statement"

    problem_prompt = {
        "role": "system",
        "content": str(problem),
    }

    history.append(language_selection_prompt)
    history.append(interviewer_behavior_prompt)
    history.append(problem_prompt)
    
    response = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            *history,
        ],
    )

    text = response.choices[0].message.content

    history.append({
        "role": "assistant",
        "content": text,
    })

    # Save back into the cache
    chat_id = random.randint(1, 1000000)  # Generate a random chat_id
    conversation_cache[str(chat_id)] = {"language": language, "history": history}

    return chat_id, text

async def process_chat(
    chat_id: str,
    image_file: UploadFile,
    speech_file: UploadFile,
    speech_content: str = None
):
    # Fetch history from cache
    cached_cache = conversation_cache.get(chat_id)

    if not cached_cache:
        raise ValueError("Chat ID not found in cache.")

    history = cached_cache["history"]
    language = cached_cache["language"]

    # Process speech
    if not speech_content:
        # If no speech content is provided, transcribe the speech file
        speech_content = await transcribe_speech(speech_file, language=language)

    user_message = [
        {"type": "text", "text": speech_content}
    ]

    if image_file:
        # Mock a image upload first and get the file_url
        image_url = await upload_image_to_s3(image_file)

        user_message = [
            {"type": "text", "text": speech_content},
            {"type": "image_url",
                "image_url": {
                    "url": image_url,
                }
            }
        ]

    response = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=[
            *history,
            {
                "role": "user",
                "content": user_message,
            }
        ],
    )

    text = response.choices[0].message.content

    # Update conversation history
    history.append({
        "role": "user",
        "content": user_message,
    })
    history.append({
        "role": "assistant",
        "content": text,
    })

    # Save back into the cache
    conversation_cache[chat_id] = history

    return text, history

async def load_json_file(file_path: str) -> dict:
    print(f"Loading JSON file from {file_path}")
    async with aiofiles.open(file_path, mode='r') as f:
        content = await f.read()
        return json.loads(content)

async def upload_image_to_s3(img_file: UploadFile) -> str:
    async with aiofiles.open("image_url.txt", mode='r') as f:
        return await f.read()

