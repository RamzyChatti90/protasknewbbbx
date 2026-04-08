import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ICategory } from 'app/entities/category/category.model';
import { Priority } from 'app/entities/enumerations/priority.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface ITask {
  id: number;
  title?: string | null;
  description?: string | null;
  priority?: keyof typeof Priority | null;
  status?: keyof typeof Status | null;
  dueDate?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  assignee?: Pick<IUser, 'id' | 'login'> | null;
  category?: Pick<ICategory, 'id'> | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
