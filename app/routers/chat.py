from typing import Optional
from fastapi import APIRouter, HTTPException, UploadFile, File, Form
from app.services.chat_service import process_chat, create_chat
from pydantic import BaseModel

router = APIRouter()

class CreateChatRequest(BaseModel):
    problem_id: str
    language: str

@router.post("")
async def create_chat_route(request: CreateChatRequest):
    try:
        # Process chat
        chat_id, response = await create_chat(
            problem_id=request.problem_id,
            language=request.language)

        # Return response
        return {"chat_id": chat_id, "response": response}

    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

@router.post("/{chat_id}")
async def chat(
    chat_id: str,
    image: Optional[UploadFile] = File(None),
    speech_file: Optional[UploadFile] = File(None),
    speech_content: Optional[str] = Form(None)
):
    try:
        # Process chat
        text, history = await process_chat(
            chat_id=chat_id,
            image_file=image, 
            speech_file=speech_file, 
            speech_content=speech_content)

        # Return response
        return {"response": text, "history": history}

    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))
