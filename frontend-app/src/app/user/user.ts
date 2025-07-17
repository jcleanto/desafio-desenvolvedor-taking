export interface User {
  id?: string;
  username: string;
  firstName: string | null;
  lastName: string | null;
  email: string;
  credentials: string | null;
}

export interface UserResponse {
  items: User[];
  totalCount: number;
}