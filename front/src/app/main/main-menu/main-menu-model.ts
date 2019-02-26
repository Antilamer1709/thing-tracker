export class MessageDTO {
  size: number;
  hasFile: boolean;
  readed: boolean;
  text: string;

  constructor(size: number, hasFile: boolean, readed: boolean, text: string) {
    this.size = size;
    this.hasFile = hasFile;
    this.readed = readed;
    this.text = text;
  }
}
