import dayjs from 'dayjs/esm';

import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: 9181,
  title: 'or consequently',
};

export const sampleWithPartialData: ITask = {
  id: 2746,
  title: 'past personalise croon',
  description: '../fake-data/blob/hipster.txt',
  dueDate: dayjs('2026-04-07'),
  createdAt: dayjs('2026-04-08T03:49'),
};

export const sampleWithFullData: ITask = {
  id: 13396,
  title: 'where wherever',
  description: '../fake-data/blob/hipster.txt',
  priority: 'MEDIUM',
  status: 'DONE',
  dueDate: dayjs('2026-04-08'),
  createdAt: dayjs('2026-04-08T15:30'),
};

export const sampleWithNewData: NewTask = {
  title: 'expostulate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
