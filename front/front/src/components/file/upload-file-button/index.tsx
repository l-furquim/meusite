import React, { useRef } from 'react';
import { Button } from '@/components/ui/button';

const UploadButton: React.FC<{ onFileSelect: (file: File) => void }> = ({ onFileSelect }) => {
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      onFileSelect(file);
    }
  };

  const handleButtonClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click(); // Simula o clique no input de arquivo ao clicar no botão
    }
  };

  return (
    <>
      
      <Button size="sm" onClick={handleButtonClick}>
        Selecionar Imagem
      </Button>
      <input
        type="file"
        ref={fileInputRef}
        onChange={handleFileInputChange}
        style={{ display: 'none' }}
        accept="image/*" // Aceita somente arquivos de imagem
      />
    </>
  );
};

export default UploadButton;