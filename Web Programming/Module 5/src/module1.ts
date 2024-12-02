// export default function getA() { return 20; }

import { faker } from '@faker-js/faker';

export interface User {
    id: string;
    name: string;
    email: string;
    birthday: Date;
    occupation: String;
}
// Enforce that function returns a User
export function generateUser(): User {
    const firstName = faker.person.firstName();
    const lastName = faker.person.lastName();

    return {
        id: faker.string.uuid(),
        name: `${firstName} ${lastName}`,
        email: faker.internet.email(),
        birthday: faker.date.past( {years: 30} ),
        occupation: faker.person.jobTitle()

    }
}