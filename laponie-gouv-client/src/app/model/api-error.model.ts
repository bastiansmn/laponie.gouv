export default interface ApiError {
  timestamp: number;
  message: string;
  devMessage: string;
  httpStatusString: string;
  httpStatus: number;
}
