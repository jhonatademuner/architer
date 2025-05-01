from fastapi import FastAPI
from app.routers import speech, chat

app = FastAPI()

# Include routers
app.include_router(speech.router, prefix="/api/speech", tags=["Speech"])
app.include_router(chat.router, prefix="/api/chat", tags=["Chat"])

# Health check endpoint
@app.get("/api/health")
def health_check():
    return {"status": "ok"}